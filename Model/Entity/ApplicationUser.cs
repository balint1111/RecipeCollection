using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace EFGetStarted.Model.Entity
{
    public class ApplicationUser: IdentityUser<int>
    {
        [Required]
        public string Name { get; set; }

        [Required]
        public string Settlement { get; set; }

        [Required]
        public string Country { get; set; }

        [Required]
        public bool Deleted { get; set; } = false;
    }
    
    public class ApplicationUserTypeConfiguration : IEntityTypeConfiguration<ApplicationUser>
    {
        public void Configure(EntityTypeBuilder<ApplicationUser> builder)
        {
            builder.HasQueryFilter(e => !e.Deleted);
        }
    }
}
