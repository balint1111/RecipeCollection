using EFGetStarted.Attributes;
using EFGetStarted.Exceptions;
using EFGetStarted.Mapper;
using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services.Interface;
using EFGetStarted.UnitOfWork;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.Services
{
    public class RecipeService : IRecipeService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly RecipeMapper _recipeMapper;
        private readonly RecipeFullMapper _recipeFullMapper;
        private readonly CurrentUser _currentUser;

        public RecipeService(IUnitOfWork unitOfWork, RecipeMapper recipeMapper, RecipeFullMapper recipeFullMapper,
            CurrentUser currentUser)
        {
            _unitOfWork = unitOfWork;
            _recipeMapper = recipeMapper;
            _recipeFullMapper = recipeFullMapper;
            _currentUser = currentUser;
        }

        public async Task<RecipeGetDto> GetById(int id)
        {
            var recipe = await _unitOfWork.GetRepository<Recipe>().GetAll()
                .Include(it => it.IngredientGroups)
                .ThenInclude(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialCategory)
                .Include(it => it.IngredientGroups)
                .ThenInclude(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .FirstOrDefaultAsync(it => it.Id == id);
            if (recipe == null)
                throw new BadRequestException("There is no recipe with this id " + id + "!");
            return _recipeMapper.ToGetDto(recipe);
        }

        public async Task<List<RecipeGetDto>> GetAll(bool showDeleted)
        {
            var recipes = await _unitOfWork.GetRepository<Recipe>().GetAll()
                .Let(it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Include(it => it.IngredientGroups)
                .ThenInclude(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialCategory)
                .Include(it => it.IngredientGroups)
                .ThenInclude(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .ToListAsync();
            return _recipeMapper.ToList(recipes);
        }

        public async Task<PageResponseDto<RecipeGetDto>> GetAllPageable(
            bool showDeleted,
            PageableDto pageable
        )
        {
            var query = _unitOfWork.GetRepository<Recipe>().GetAll()
                .Let(it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Include(it => it.IngredientGroups)
                .ThenInclude(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialCategory)
                .Include(it => it.IngredientGroups)
                .ThenInclude(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialAllergens)
                .ThenInclude(it => it.Allergen);
            return await pageable.ToPage(query, _recipeMapper);
        }


        public async Task AddFavorite(int recipeId)
        {
            var recipe = await _unitOfWork.GetRepository<Recipe>().GetAll()
                .FirstOrDefaultAsync(it => it.Id == recipeId);
            if (recipe == null)
                throw new BadRequestException("There is no recipe with this id " + recipeId + "!");
            await _unitOfWork.GetRepository<UserFavorite>()
                .Create(_recipeMapper.RecipeIdAndUserIdToUserFavorite(recipeId,
                    (int)(await _currentUser.UserIdAsync())!));
            await _unitOfWork.SaveChangesAsync();
        }

        public async Task DeleteFavorite(int recipeId)
        {
            var userId = await _currentUser.UserIdAsync();
            var userFavorite = await _unitOfWork.GetRepository<UserFavorite>().GetAll()
                .FirstOrDefaultAsync(it => it.RecipeId == recipeId && it.UserId == userId);
            if (userFavorite == null)
                throw new BadRequestException("There is no recipe for the user with this id " + recipeId + "!");
            await _unitOfWork.GetRepository<UserFavorite>()
                .Delete(userFavorite.Id);
            await _unitOfWork.SaveChangesAsync();
        }

        public async Task<List<RecipeGetDto>> GetByMaterialId(int materialId, bool showDeleted)
        {
            var recipes = await _unitOfWork.GetRepository<Recipe>().GetAll()
                .Let(it => showDeleted ? it.IgnoreQueryFilters() : it)
                .Include(it => it.IngredientGroups)
                .ThenInclude(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialCategory)
                .Include(it => it.IngredientGroups)
                .ThenInclude(it => it.Ingredients)
                .ThenInclude(it => it.Material.MaterialAllergens)
                .ThenInclude(it => it.Allergen)
                .Where(recipe =>
                    recipe.IngredientGroups.Any(ig =>
                        ig.Ingredients.Any(i =>
                            i.Material.Id == materialId
                        )
                    )
                )
                .OrderBy(it => it.CookingDuration)
                .ToListAsync();
            return _recipeMapper.ToList(recipes);
        }


        public async Task Create(RecipePostDto recipe)
        {
            var created = await _unitOfWork.GetRepository<Recipe>()
                .Create(_recipeMapper.ToEntity(recipe));
            await _unitOfWork.SaveChangesAsync();
            var ingredientGroups = _unitOfWork.GetRepository<IngredientGroup>().GetAll()
                .Where(it => recipe.IngredientGroupIds.Contains(it.Id)).ToList();
            foreach (var ingredientGroup in ingredientGroups)
            {
                ingredientGroup.RecipeId = created.Id;
            }

            await _unitOfWork.SaveChangesAsync();
        }

        public async Task CreateFull(RecipeFullPostDto recipe)
        {
            var recipeEntity = _recipeFullMapper.ToEntity(recipe);
            await _unitOfWork.GetRepository<Recipe>()
                .Create(recipeEntity);

            await _unitOfWork.SaveChangesAsync();
        }

        [Transactional]
        public async Task UpdateFull(RecipeFullPutDto recipe)
        {
            var oldIngredientGroups = _unitOfWork.GetRepository<IngredientGroup>().GetAll()
                .Include(it => it.Ingredients)
                .Where(it => it!.RecipeId == recipe.Id).ToList();
            foreach (var oldIngredientGroup in oldIngredientGroups)
            {
                oldIngredientGroup.RecipeId = null;
                foreach (var ingredient in oldIngredientGroup.Ingredients)
                {
                    ingredient.IngredientGroupId = null;
                }
            }

            await _unitOfWork.SaveChangesAsync();
            _unitOfWork.Context().ChangeTracker.Clear();

            var recipeEntity = _recipeFullMapper.ToEntity(recipe);
            _unitOfWork.GetRepository<Recipe>().Update(recipeEntity);
            await _unitOfWork.SaveChangesAsync();
        }

        public async Task Update(RecipePutDto recipe)
        {
            var oldIngredientGroups = _unitOfWork.GetRepository<IngredientGroup>().GetAll()
                .Where(it => it!.RecipeId == recipe.Id).ToList();
            _unitOfWork.GetRepository<Recipe>().Update(_recipeMapper.ToEntity(recipe));
            foreach (var oldIngredientGroup in oldIngredientGroups)
            {
                oldIngredientGroup!.RecipeId = null;
            }

            await _unitOfWork.SaveChangesAsync();
            var ingredientGroups = _unitOfWork.GetRepository<IngredientGroup>().GetAll()
                .Where(it => recipe.IngredientGroupsIds.Contains(it.Id)).ToList();
            foreach (var ingredientGroup in ingredientGroups)
            {
                ingredientGroup!.RecipeId = recipe.Id;
            }

            await _unitOfWork.SaveChangesAsync();
        }

        public async Task Delete(int id)
        {
            await _unitOfWork.GetRepository<Recipe>().DeleteSoft(id);
            await _unitOfWork.SaveChangesAsync();
        }
    }
}