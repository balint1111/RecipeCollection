using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;

namespace EFGetStarted.Mapper;

public class MaterialMapper : GenericMapper<Material, MaterialPostDto, MaterialPutDto, MaterialGetDto>
{
    private readonly AllergenMapper _allergenMapper;
    private readonly MaterialCategoryMapper _materialCategoryMapper;

    public MaterialMapper(AllergenMapper allergenMapper, MaterialCategoryMapper materialCategoryMapper)
    {
        _allergenMapper = allergenMapper;
        _materialCategoryMapper = materialCategoryMapper;
    }
    public override Material ToEntity(MaterialPostDto dto)
    {
        return new Material
        {
            Name = dto.Name,
            MaterialCategoryId = dto.MaterialCategoryId
        };
    }

    public override Material ToEntity(MaterialPutDto dto)
    {
        return new Material
        {
            Id = dto.Id,
            Name = dto.Name,
            MaterialCategoryId = dto.MaterialCategoryId
        };
    }

    public override MaterialGetDto ToGetDto(Material entity)
    {
        foreach (var entityMaterialAllergen in entity.MaterialAllergens)
        {
            Console.WriteLine(entityMaterialAllergen.Allergen.Id);
        }

        return new MaterialGetDto()
        {
            Id = entity.Id,
            Name = entity.Name,
            Allergens = entity.MaterialAllergens.Select(it => _allergenMapper.ToGetDto(it.Allergen)).ToList(),
            MaterialCategory = _materialCategoryMapper.ToGetDto(entity.MaterialCategory)
        };
    }

    public List<MaterialAllergen> AllergenIdsToMaterialAllergens(List<int> allergenIds, int materialId)
    {
        return allergenIds.Select(it => new MaterialAllergen
        {
            MaterialId = materialId,
            AllergenId = it
        }).ToList();
    }
}