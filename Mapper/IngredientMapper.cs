using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;

namespace EFGetStarted.Mapper;

public class IngredientMapper: GenericMapper<Ingredient, IngredientPostDto, IngredientPutDto, IngredientGetDto>
{
    
    
    private readonly MaterialMapper _materialMapper;

    public IngredientMapper(MaterialMapper materialMapper)
    {
        _materialMapper = materialMapper;
    }

    public override Ingredient ToEntity(IngredientPostDto dto)
    {
        return new Ingredient
        {
            Quantity = dto.Quantity,
            Unit = dto.Unit,
            MaterialId= dto.MaterialId
        };
    }

    public override Ingredient ToEntity(IngredientPutDto dto)
    {
        return new Ingredient
        {
            Id = dto.Id,
            Quantity = dto.Quantity,
            Unit = dto.Unit,
            MaterialId= dto.MaterialId
        };
    }

    public override IngredientGetDto ToGetDto(Ingredient entity)
    {
        return new IngredientGetDto()
        {
            Id = entity.Id,
            Quantity = entity.Quantity,
            Unit = entity.Unit,
            Material = _materialMapper.ToGetDto(entity.Material)
        };
    }
}