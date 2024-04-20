using System.Security.Claims;
using EFGetStarted.Exceptions;
using EFGetStarted.Mapper;
using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services.Interface;
using EFGetStarted.UnitOfWork;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.Services
{
    public class IngredientGroupService : IIngredientGroupService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly IngredientGroupMapper _ingredientGroupMapper;

        public IngredientGroupService(IUnitOfWork unitOfWork, IngredientGroupMapper ingredientGroupMapper)
        {
            _unitOfWork = unitOfWork;
            _ingredientGroupMapper = ingredientGroupMapper;
        }

        public async Task<IngredientGroupGetDto> GetById(int id)
        {
            var ingredientGroup = await _unitOfWork.GetRepository<IngredientGroup>().GetAll()
                .Include(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialCategory)
                .Include(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .FirstOrDefaultAsync(it => it.Id == id);
            if (ingredientGroup == null)
                throw new BadRequestException("There is no ingredientGroup with this id " + id + "!");
            return _ingredientGroupMapper.ToGetDto(ingredientGroup);
        }
        
        public async Task<List<IngredientGroupGetDto>> GetAll(bool showDeleted)
        {
            var ingredientGroups = await _unitOfWork.GetRepository<IngredientGroup>().GetAll()
                .Let( it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Include(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialCategory)
                .Include(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .ToListAsync();
            return _ingredientGroupMapper.ToList(ingredientGroups);
        }

        public async Task Create(IngredientGroupPostDto ingredientGroup)
        {
            var created = await _unitOfWork.GetRepository<IngredientGroup>()
                .Create(_ingredientGroupMapper.ToEntity(ingredientGroup));
            await _unitOfWork.SaveChangesAsync();
            var ingredients = _unitOfWork.GetRepository<Ingredient>().GetAll()
                .Where(it => ingredientGroup.IngredientIds.Contains(it!.Id)).ToList();
            foreach (var ingredient in ingredients)
            {
                ingredient!.IngredientGroupId = created.Id;
            }

            await _unitOfWork.SaveChangesAsync();
        }

        public async Task Update(IngredientGroupPutDto ingredientGroup)
        {
            var oldIngredients = _unitOfWork.GetRepository<Ingredient>().GetAll()
                .Where(it => it!.IngredientGroupId == ingredientGroup.Id).ToList();
            _unitOfWork.GetRepository<IngredientGroup>().Update(_ingredientGroupMapper.ToEntity(ingredientGroup));
            foreach (var oldIngredient in oldIngredients)
            {
                oldIngredient!.IngredientGroupId = null;
            }

            await _unitOfWork.SaveChangesAsync();
            var ingredients = _unitOfWork.GetRepository<Ingredient>().GetAll()
                .Where(it => ingredientGroup.IngredientIds.Contains(it!.Id)).ToList();
            foreach (var ingredient in ingredients)
            {
                ingredient!.IngredientGroupId = ingredientGroup.Id;
            }

            await _unitOfWork.SaveChangesAsync();
        }

        public async Task Delete(int id)
        {
            await _unitOfWork.GetRepository<IngredientGroup>().DeleteSoft(id);
            await _unitOfWork.SaveChangesAsync();
        }
    }
}