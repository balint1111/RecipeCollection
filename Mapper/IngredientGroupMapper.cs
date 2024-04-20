using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;

namespace EFGetStarted.Mapper;

public class IngredientGroupMapper : GenericMapper<IngredientGroup, IngredientGroupPostDto, IngredientGroupPutDto,
    IngredientGroupGetDto>
{
    private readonly IngredientMapper _ingredientMapper;

    public IngredientGroupMapper(IngredientMapper ingredientMapper)
    {
        _ingredientMapper = ingredientMapper;
    }

    public override IngredientGroup ToEntity(IngredientGroupPostDto dto)
    {
        return new IngredientGroup
        {
            Name = dto.Name,
        };
    }

    public override IngredientGroup ToEntity(IngredientGroupPutDto dto)
    {
        return new IngredientGroup
        {
            Id = dto.Id,
            Name = dto.Name,
        };
    }

    public override IngredientGroupGetDto ToGetDto(IngredientGroup entity)
    {
        return new IngredientGroupGetDto()
        {
            Id = entity.Id,
            Name = entity.Name,
            Ingredients = entity.Ingredients.Select(it => _ingredientMapper.ToGetDto(it)).ToList()
        };
    }
}