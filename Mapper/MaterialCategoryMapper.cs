using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;

namespace EFGetStarted.Mapper;

public class MaterialCategoryMapper : GenericMapper<MaterialCategory, MaterialCategoryPostDto, MaterialCategoryPutDto, MaterialCategoryGetDto>
{
    public override MaterialCategory ToEntity(MaterialCategoryPostDto dto)
    {
        return new MaterialCategory
        {
            Name = dto.Name
        };
    }
    
    public override MaterialCategory ToEntity(MaterialCategoryPutDto dto)
    {
        return new MaterialCategory
        {
            Id = dto.Id,
            Name = dto.Name
        };
    }
    
    public override MaterialCategoryGetDto ToGetDto(MaterialCategory entity)
    {
        return new MaterialCategoryGetDto()
        {
            Id = entity.Id,
            Name = entity.Name
        };
    }
}