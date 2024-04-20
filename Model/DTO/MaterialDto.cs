namespace EFGetStarted.Model.DTO;

public class MaterialPostDto : AbstractPostDto
{
    public string Name { get; set; }
    public List<int> allergenIds { get; set; }
    public int MaterialCategoryId { get; set; }
}

public class MaterialPutDto : AbstractPutDto
{
    public string Name { get; set; }
    public List<int> allergenIds { get; set; }
    public int MaterialCategoryId { get; set; }
}

public class MaterialGetDto : AbstractGetDto
{
    public string Name { get; set; }
    public List<AllergenGetDto> Allergens { get; set; }
    public MaterialCategoryGetDto MaterialCategory { get; set; }
}