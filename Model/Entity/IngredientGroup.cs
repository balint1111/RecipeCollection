using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity;

public class IngredientGroup : AbstractEntity
{
    [Required]
    public string Name { get; set; }
    public List<Ingredient> Ingredients { get; set; }
    
    public int? RecipeId { get; set; } = null;
}

public class IngredientGroupTypeConfiguration : IEntityTypeConfiguration<IngredientGroup>
{
    public void Configure(EntityTypeBuilder<IngredientGroup> builder)
    {
        builder.HasQueryFilter(e => !e.Deleted);
    }
}