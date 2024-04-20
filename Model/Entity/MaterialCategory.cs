using System.ComponentModel.DataAnnotations;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity;

public class MaterialCategory : AbstractEntity
{
    [Required]
    public string Name { get; set; }
}

public class MaterialCategoryTypeConfiguration : IEntityTypeConfiguration<MaterialCategory>
{
    public void Configure(EntityTypeBuilder<MaterialCategory> builder)
    {
        builder.HasQueryFilter(e => !e.Deleted);
    }
}