using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity;

public class UserAllergen : AbstractEntity
{
    [Required]
    public int AllergenId { get; set; }
    [ForeignKey("AllergenId")]
    public Allergen Allergen { get; set; }
    [Required]
    public int UserId { get; set; }
    [ForeignKey("UserId")]
    public ApplicationUser User { get; set; }
}

public class UserAllergenTypeConfiguration : IEntityTypeConfiguration<UserAllergen>
{
    public void Configure(EntityTypeBuilder<UserAllergen> builder)
    {
        builder.HasQueryFilter(e => !e.Deleted);
    }
}