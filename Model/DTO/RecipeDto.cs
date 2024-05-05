using EFGetStarted.Attributes;

namespace EFGetStarted.Model.DTO;

public class RecipePostDto : AbstractPostDto
{
    public string Code { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public List<int> IngredientGroupIds { get; set; }
    public long PreparationDuration { get; set; } = 0;
    public long CookingDuration { get; set; }
}

public class RecipeFullPostDto : AbstractPostDto
{
    public string Code { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    
    public List<IngredientGroupFullPostDto> IngredientGroups { get; set; }
    public long PreparationDuration { get; set; } = 0;
    public long CookingDuration { get; set; }
}

public class RecipePutDto : AbstractPutDto
{
    public string Code { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public List<int> IngredientGroupsIds { get; set; }
    public long PreparationDuration { get; set; } = 0;
    public long CookingDuration { get; set; }
}

public class RecipeFullPutDto : AbstractPutDto
{
    public string Code { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public List<IngredientGroupFullPutDto> IngredientGroups { get; set; }
    public long PreparationDuration { get; set; } = 0;
    public long CookingDuration { get; set; }
}
public class RecipeGetDto : AbstractGetDto
{
    [RecipeCodeValidation]
    public string Code { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public List<IngredientGroupGetDto> IngredientGroups { get; set; }
    public long PreparationDuration { get; set; }
    public long CookingDuration { get; set; }

    public long TotalDuration => PreparationDuration + CookingDuration;
    
    public bool IsFavorite { get; set; }
}

