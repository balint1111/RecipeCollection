using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity;

public class Allergen : AbstractEntity
{
    [Required]
    public string Name { get; set; }
    [Required]
    public string ImgBase64 { get; set; }
}

public class AllergenTypeConfiguration : IEntityTypeConfiguration<Allergen>
{
    public void Configure(EntityTypeBuilder<Allergen> builder)
    {
        builder.HasQueryFilter(e => !e.Deleted);
    }
}