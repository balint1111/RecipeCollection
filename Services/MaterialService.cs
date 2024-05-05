using EFGetStarted.Exceptions;
using EFGetStarted.Mapper;
using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services.Interface;
using EFGetStarted.UnitOfWork;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.Services
{
    public class MaterialService : IMaterialService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly MaterialMapper _materialMapper;
        private readonly ICacheService _cacheService;

        public MaterialService(IUnitOfWork unitOfWork, MaterialMapper materialMapper, ICacheService cacheService)
        {
            _unitOfWork = unitOfWork;
            _materialMapper = materialMapper;
            _cacheService = cacheService;
        }

        public async Task<MaterialGetDto> GetById(int id)
        {
            if (!_cacheService.TryGetValue(id, out Material? material))
            {
                material = await _unitOfWork.GetRepository<Material>().GetAll()
                    .Include(it => it.MaterialCategory)
                    .Include(it => it.MaterialAllergens)
                    .ThenInclude(it => it.Allergen)
                    .FirstOrDefaultAsync(it => it.Id == id);
            }

            if (material == null)
                throw new BadRequestException("There is no material with this id " + id + "!");
            return _materialMapper.ToGetDto(material);
        }
        
        public async Task<List<MaterialGetDto>> GetAll(bool showDeleted)
        {
            var materials = await _unitOfWork.GetRepository<Material>().GetAll()
                .Let( it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Include(it => it.MaterialCategory)
                .Include(it => it.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .ToListAsync();
            return _materialMapper.ToList(materials);
        }
        
        public async Task<PageResponseDto<MaterialGetDto>> GetAllPageable(
            bool showDeleted,
            PageableDto pageable
        )
        {
            var query = _unitOfWork.GetRepository<Material>().GetAll()
                .Let( it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Include(it => it.MaterialCategory)
                .Include(it => it.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .Where(it => it.Name.Contains(pageable.Filter));
            return await pageable.ToPage(query, _materialMapper);
        }
        
        public async Task<List<MaterialGetDto>> GetAllByCategory(int materialCategoryId, bool showDeleted)
        {
            var materials = await _unitOfWork.GetRepository<Material>().GetAll()
                .Let( it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Include(it => it.MaterialCategory)
                .Include(it => it.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .Where(it => it.MaterialCategoryId == materialCategoryId)
                .ToListAsync();
            return _materialMapper.ToList(materials);
        }
        
        public async Task<List<MaterialGetDto>> GetAllByAllergens(List<int> allergenIds, bool showDeleted)
        {
            var materials = await _unitOfWork.GetRepository<Material>().GetAll()
                .Let( it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Include(it => it.MaterialCategory)
                .Include(it => it.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .Where(it => it.MaterialAllergens.Any( ma => allergenIds.Contains(ma.AllergenId)))
                .ToListAsync();
            return _materialMapper.ToList(materials);
        }


        public async Task Create(MaterialPostDto material)
        {
            var created = await _unitOfWork.GetRepository<Material>()
                .Create(_materialMapper.ToEntity(material));

            await _unitOfWork.SaveChangesAsync();
            foreach (var materialAllergen in _materialMapper.AllergenIdsToMaterialAllergens(material.allergenIds,
                         created.Id))
            {
                await _unitOfWork.GetRepository<MaterialAllergen>().Create(materialAllergen);
            }
            await _unitOfWork.SaveChangesAsync();
            created = await _unitOfWork.GetRepository<Material>().GetAll()
                .Include(it => it.MaterialCategory)
                .Include(it => it.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .FirstOrDefaultAsync(it => it.Id == created.Id);
            if (created != null) _cacheService.Set(created.Id, created);
        }

        public async Task Update(MaterialPutDto material)
        {
            var old = _unitOfWork.GetRepository<Material>().GetAll()
                .Include(it => it.MaterialAllergens).ThenInclude(it => it.Allergen)
                .FirstOrDefault(it => it.Id == material.Id);
            var oldAllergenIds = old!.MaterialAllergens.Select(it2 => it2.AllergenId).ToList();
            var newAllergenIds = material.allergenIds.Where(it => !oldAllergenIds.Contains(it)).ToList();
            var toDeleteAllergenIds = oldAllergenIds.Where(it => !newAllergenIds.Contains(it)).ToList();
            _unitOfWork.GetRepository<Material>().Update(_materialMapper.ToEntity(material));
            var updated = await _unitOfWork.GetRepository<Material>().GetAll()
                .Include(it => it.MaterialCategory)
                .Include(it => it.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .FirstOrDefaultAsync(it => it.Id == material.Id);
            
            foreach (var toDeleteAllergenId in toDeleteAllergenIds)
            {
                var toDeleteMaterialAllergen = _unitOfWork.GetRepository<MaterialAllergen>()
                    .GetAll().First(it => it!.AllergenId == toDeleteAllergenId);
                await _unitOfWork.GetRepository<Material>().Delete(toDeleteMaterialAllergen!.Id);
            }

            foreach (var materialAllergen in _materialMapper.AllergenIdsToMaterialAllergens(newAllergenIds, updated!.Id))
            {
                await _unitOfWork.GetRepository<MaterialAllergen>().Create(materialAllergen);
            }

            await _unitOfWork.SaveChangesAsync();
            
            
            _cacheService.Set(updated!.Id, updated);
        }

        public async Task Delete(int id)
        {
            await _unitOfWork.GetRepository<Material>().DeleteSoft(id);
            await _unitOfWork.SaveChangesAsync();
            _cacheService.Remove(id);
        }
    }
}