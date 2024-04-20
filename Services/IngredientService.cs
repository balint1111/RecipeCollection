using EFGetStarted.Exceptions;
using EFGetStarted.Mapper;
using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services.Interface;
using EFGetStarted.UnitOfWork;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.Services
{
    public class IngredientService : IIngredientService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly IngredientMapper _ingredientMapper;

        public IngredientService(IUnitOfWork unitOfWork, IngredientMapper ingredientMapper)
        {
            _unitOfWork = unitOfWork;
            _ingredientMapper = ingredientMapper;
        }

        public async Task<IngredientGetDto> GetById(int id)
        {
            var ingredient = await _unitOfWork.GetRepository<Ingredient>().GetAll()
                .Include(it => it.Material.MaterialCategory)
                .Include(it => it.Material.MaterialAllergens)
                .ThenInclude(it => it.Allergen).FirstOrDefaultAsync(it => it.Id == id);
            if (ingredient == null)
                throw new BadRequestException("There is no ingredient with this id " + id + "!");
            return _ingredientMapper.ToGetDto(ingredient);
        }
        
        public async Task<List<IngredientGetDto>> GetAll(bool showDeleted)
        {
            var ingredients = await _unitOfWork.GetRepository<Ingredient>().GetAll()
                .Let( it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Include(it => it.Material)
                .ThenInclude(it => it.MaterialCategory)
                .Include(it => it.Material)
                .ThenInclude(it => it.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .ToListAsync();
            return _ingredientMapper.ToList(ingredients);
        }

        public async Task Create(IngredientPostDto ingredient)
        {
            await _unitOfWork.GetRepository<Ingredient>().Create(_ingredientMapper.ToEntity(ingredient));
            await _unitOfWork.SaveChangesAsync();
        }

        public async Task Update(IngredientPutDto ingredient)
        {
            _unitOfWork.GetRepository<Ingredient>().Update(_ingredientMapper.ToEntity(ingredient));
            await _unitOfWork.SaveChangesAsync();
        }

        public async Task Delete(int id)
        {
            await _unitOfWork.GetRepository<Ingredient>().DeleteSoft(id);
            await _unitOfWork.SaveChangesAsync();
        }
    }
}