using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Repository;
using EFGetStarted.UnitOfWork;

namespace EFGetStarted.Mapper;

public class RecipeMapper : GenericMapper<Recipe, RecipePostDto, RecipePutDto, RecipeGetDto>
{
    private readonly IngredientGroupMapper _ingredientGroupMapper;
    private readonly IGenericRepository<UserFavorite> _userFavoriteRepository;
    private readonly CurrentUser _currentUser;

    public RecipeMapper(IngredientGroupMapper ingredientGroupMapper, IUnitOfWork unitOfWork, CurrentUser currentUser)
    {
        _ingredientGroupMapper = ingredientGroupMapper;
        _userFavoriteRepository = unitOfWork.GetRepository<UserFavorite>();
        _currentUser = currentUser;
    }

    public override Recipe ToEntity(RecipePostDto dto)
    {
        return new Recipe
        {
            Name = dto.Name,
            Code = dto.Code,
            Description = dto.Description,
            PreparationDuration = dto.PreparationDuration,
            CookingDuration = dto.CookingDuration,
            CreatedByUserId = (int)_currentUser.UserId()!,
            ImgBase64 = dto.ImgBase64
        };
    }

    public override Recipe ToEntity(RecipePutDto dto)
    {
        return new Recipe
        {
            Name = dto.Name,
            Code = dto.Code,
            Description = dto.Description,
            PreparationDuration = dto.PreparationDuration,
            CookingDuration = dto.CookingDuration,
            ImgBase64 = dto.ImgBase64
        }.Let( it =>
        {
            if (dto.Id != null) it.Id = (int)dto.Id;
            return it;
        });
    }

    public override RecipeGetDto ToGetDto(Recipe entity)
    {
        var userId = _currentUser.UserId();
        return new RecipeGetDto()
        {
            Id = entity.Id,
            Name = entity.Name,
            Code = entity.Code,
            Description = entity.Description,
            PreparationDuration = entity.PreparationDuration,
            CookingDuration = entity.CookingDuration,
            IngredientGroups = entity.IngredientGroups.Select(it => _ingredientGroupMapper.ToGetDto(it)).ToList(),
            IsFavorite = _userFavoriteRepository.GetAll()
                .Where(it => it.UserId == userId && it.RecipeId == entity.Id)
                .ToList().Count != 0,
            createdBy = entity.CreatedByUserId,
            ImgBase64 = entity.ImgBase64
        };
    }

    public UserFavorite RecipeIdAndUserIdToUserFavorite(int recipeId, int userId)
    {
        return new UserFavorite()
        {
            RecipeId = recipeId,
            UserId = userId
        };
    }
}