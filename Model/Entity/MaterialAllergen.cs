using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity;

public class MaterialAllergen : AbstractEntity
{
    [Required]
    public int AllergenId { get; set; }
    [ForeignKey("AllergenId")]
    public Allergen Allergen { get; set; }
    [Required]
    public int MaterialId { get; set; }
    [ForeignKey("MaterialId")]
    public Material Material { get; set; }
}

public class MaterialAllergenTypeConfiguration : IEntityTypeConfiguration<MaterialAllergen>
{
    public void Configure(EntityTypeBuilder<MaterialAllergen> builder)
    {
        builder.HasQueryFilter(e => !e.Deleted);
    }
}