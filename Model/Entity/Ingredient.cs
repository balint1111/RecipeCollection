using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity;

public class Ingredient : AbstractEntity
{
    [Required]
    public int MaterialId { get; set; }
    [ForeignKey("MaterialId")]
    public Material Material { get; set; }
    [Required]
    public string Unit { get; set; }
    [Required]
    [Precision(18, 2)]
    public decimal Quantity { get; set; }

    public int? IngredientGroupId { get; set; } = null;
}

public class IngredientTypeConfiguration : IEntityTypeConfiguration<Ingredient>
{
    public void Configure(EntityTypeBuilder<Ingredient> builder)
    {
        builder.HasQueryFilter(e => !e.Deleted);
    }
}