namespace EFGetStarted.Model.DTO;

public class IngredientPostDto : AbstractPostDto
{
    public int MaterialId { get; set; }
    public string Unit { get; set; }
    public decimal Quantity { get; set; }
}

public class IngredientPutDto : AbstractPutDto
{
    public int MaterialId { get; set; }
    public string Unit { get; set; }
    public decimal Quantity { get; set; }
}

public class IngredientGetDto : AbstractGetDto
{
    public MaterialGetDto Material { get; set; }
    public string Unit { get; set; }
    public decimal Quantity { get; set; }
}