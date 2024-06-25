using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using EFGetStarted.Attributes;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity;

public class Recipe : AbstractEntity
{
    [Required]
    [RecipeCodeValidation]
    public string Code { get; set; }
    [Required]
    public string Name { get; set; }
    [Required]
    public string Description { get; set; }
    public List<IngredientGroup> IngredientGroups { get; set; }
    public long PreparationDuration { get; set; }
    [Required]
    public long CookingDuration { get; set; }
    
    [Required]
    public int CreatedByUserId { get; set; }
    
    [ForeignKey("CreatedByUserId")]
    public ApplicationUser CreatedBy { get; set; }
    
    public string? ImgBase64 { get; set; }

    public long TotalDuration => PreparationDuration + CookingDuration;
    
    
}

public class RecipeTypeConfiguration : IEntityTypeConfiguration<Recipe>
{
    public void Configure(EntityTypeBuilder<Recipe> builder)
    {
        builder.HasQueryFilter(e => !e.Deleted);
    }
}

