namespace EFGetStarted.Model.DTO;

public class IngredientGroupPostDto : AbstractPostDto
{
    public string Name { get; set; }
    public List<int> IngredientIds { get; set; }
}

public class IngredientGroupFullPostDto : AbstractPostDto
{
    public string Name { get; set; }
    public List<IngredientPostDto> Ingredients { get; set; }
}

public class IngredientGroupFullPutDto : AbstractPutDto
{
    public string Name { get; set; }
    public List<IngredientPutDto> Ingredients { get; set; }
}

public class IngredientGroupPutDto : AbstractPutDto
{
    public string Name { get; set; }
    public List<int> IngredientIds { get; set; }
}

public class IngredientGroupGetDto : AbstractGetDto
{
    public string Name { get; set; }
    public List<IngredientGetDto> Ingredients { get; set; }
}