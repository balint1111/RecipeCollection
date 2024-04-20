using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity;

public class Material : AbstractEntity
{
    [Required]
    public string Name { get; set; }
    
    public List<MaterialAllergen> MaterialAllergens { get; set; }
    [Required]
    public int MaterialCategoryId { get; set; }
    [ForeignKey("MaterialCategoryId")]
    public MaterialCategory MaterialCategory { get; set; }
}

public class MaterialTypeConfiguration : IEntityTypeConfiguration<Material>
{
    public void Configure(EntityTypeBuilder<Material> builder)
    {
        builder.HasQueryFilter(e => !e.Deleted);
    }
}