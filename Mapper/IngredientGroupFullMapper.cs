using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;

namespace EFGetStarted.Mapper;

public class IngredientGroupFullMapper : GenericMapper<IngredientGroup, IngredientGroupFullPostDto,
    IngredientGroupFullPutDto, IngredientGroupGetDto>
{
    private readonly IngredientMapper _ingredientMapper;

    public IngredientGroupFullMapper(IngredientMapper ingredientMapper)
    {
        _ingredientMapper = ingredientMapper;
    }

    public override IngredientGroup ToEntity(IngredientGroupFullPostDto dto)
    {
        return new IngredientGroup
        {
            Name = dto.Name,
            Ingredients = dto.Ingredients.Select( it => _ingredientMapper.ToEntity(it)).ToList()
        };
    }

    public override IngredientGroup ToEntity(IngredientGroupFullPutDto dto)
    {
        return new IngredientGroup
        {
            Name = dto.Name,
            Ingredients = dto.Ingredients.Select( it => _ingredientMapper.ToEntity(it)).ToList()
        }.Let( it =>
        {
            if (dto.Id != null) it.Id = (int)dto.Id;
            return it;
        });
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