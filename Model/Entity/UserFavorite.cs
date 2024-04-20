using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity;

public class UserFavorite : AbstractEntity
{
    [Required]
    public int RecipeId { get; set; }
    [ForeignKey("RecipeId")]
    public Allergen Recipe { get; set; }
    [Required]
    public int UserId { get; set; }
    [ForeignKey("UserId")]
    public ApplicationUser User { get; set; }
}

public class UserFavoriteTypeConfiguration : IEntityTypeConfiguration<UserFavorite>
{
    public void Configure(EntityTypeBuilder<UserFavorite> builder)
    {
        builder.HasQueryFilter(e => !e.Deleted);
    }
}