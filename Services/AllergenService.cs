using EFGetStarted.Exceptions;
using EFGetStarted.Mapper;
using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services.Interface;
using EFGetStarted.UnitOfWork;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.Services
{
    public class AllergenService : IAllergenService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly AllergenMapper _allergenMapper;
        private readonly CurrentUser _currentUser;
        private readonly ICacheService _cacheService;
        
        public AllergenService(IUnitOfWork unitOfWork, AllergenMapper allergenMapper, CurrentUser currentUser, ICacheService cacheService)
        {
            _unitOfWork = unitOfWork;
            _allergenMapper = allergenMapper;
            _currentUser = currentUser;
            _cacheService = cacheService;
        }
        
        public async Task<AllergenGetDto> GetById(int id)
        {
            if (!_cacheService.TryGetValue(id, out Allergen? allergen))
            {
                allergen = await _unitOfWork.GetRepository<Allergen>().GetAll()
                    .FirstOrDefaultAsync(it => it.Id == id);
            }
            if (allergen == null)
                throw new BadRequestException("There is no allergen with this id " + id + "!");
            return _allergenMapper.ToGetDto(allergen);
        }
        
        public async Task<List<AllergenGetDto>> GetAll(bool showDeleted)
        {
            var allergens = await _unitOfWork.GetRepository<Allergen>().GetAll()
                .Let( it => showDeleted ? it.IgnoreQueryFilters() : it)
                .ToListAsync();
            return _allergenMapper.ToList(allergens);
        }
        
        public async Task<PageResponseDto<AllergenGetDto>> GetAllPageable(
            bool showDeleted,
            PageableDto pageable
            )
        {
            var query = _unitOfWork.GetRepository<Allergen>().GetAll()
                .Let(it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Where(allergen => allergen.Name.Contains(pageable.Filter));
            return await pageable.ToPage(query, _allergenMapper);
        }
        
        public async Task AddAllergen(int allergenId)
        {
            var allergen = await _unitOfWork.GetRepository<Allergen>().GetAll()
                .FirstOrDefaultAsync(it => it.Id == allergenId);
            if (allergen == null)
                throw new BadRequestException("There is no allergen with this id " + allergenId + "!");
            await _unitOfWork.GetRepository<UserAllergen>()
                .Create(_allergenMapper.AllergenIdAndUserIdToUserAllergen(allergenId, (int)(await _currentUser.UserIdAsync())!));
            await _unitOfWork.SaveChangesAsync();
        }
        
        public async Task DeleteAllergen(int allergenId)
        {
            var userId = await _currentUser.UserIdAsync();
            var userAllergen = await _unitOfWork.GetRepository<UserAllergen>().GetAll()
                .FirstOrDefaultAsync(it => it.AllergenId == allergenId && it.UserId == userId);
            if (userAllergen == null)
                throw new BadRequestException("There is no allergen for the user with this id " + allergenId + "!");
            await _unitOfWork.GetRepository<UserAllergen>()
                .Delete(userAllergen.Id);
            await _unitOfWork.SaveChangesAsync();
        }
        
        public async Task Create(AllergenPostDto allergen)
        {
            var created = await _unitOfWork.GetRepository<Allergen>().Create(_allergenMapper.ToEntity(allergen));
            await _unitOfWork.SaveChangesAsync();
            created = await _unitOfWork.GetRepository<Allergen>().GetAll()
                .FirstOrDefaultAsync(it => it.Id == created.Id);
            _cacheService.Set(created!.Id, created);
        }
        
        public async Task Update(AllergenPutDto allergen)
        {
            _unitOfWork.GetRepository<Allergen>().Update(_allergenMapper.ToEntity(allergen));
            await _unitOfWork.SaveChangesAsync();
            var updated = await _unitOfWork.GetRepository<Allergen>().GetAll()
                .FirstOrDefaultAsync(it => it.Id == allergen.Id);
            _cacheService.Set(updated!.Id, updated);
        }
        
        public async Task Delete(int id)
        {
            await _unitOfWork.GetRepository<Allergen>().DeleteSoft(id);
            await _unitOfWork.SaveChangesAsync();
            _cacheService.Remove(id);
        }
    }
}
