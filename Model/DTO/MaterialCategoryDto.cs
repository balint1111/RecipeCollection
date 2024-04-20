namespace EFGetStarted.Model.DTO;

public class MaterialCategoryPostDto : AbstractPostDto
{
    public string Name { get; set; }
}

public class MaterialCategoryPutDto : AbstractPutDto
{
    public string Name { get; set; }
}

public class MaterialCategoryGetDto : AbstractGetDto
{
    public string Name { get; set; }
}