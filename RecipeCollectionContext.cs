using System.ComponentModel.DataAnnotations.Schema;
using System.Reflection;
using EFGetStarted.Model.Entity;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace EFGetStarted;

public class RecipeCollectionContext : IdentityDbContext<ApplicationUser, IdentityRole<int>, int>
{
    
    public RecipeCollectionContext(DbContextOptions<RecipeCollectionContext> options) : base(options)
    {
        Database.SetCommandTimeout(60);
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.ApplyConfigurationsFromAssembly(Assembly.GetExecutingAssembly());
    
        modelBuilder.RemovePluralizingTableNameConvention();
        modelBuilder.RemoveOneToManyCascadeDeleteConvention();
    
        base.OnModelCreating(modelBuilder);
    }

    public DbSet<Recipe>? Recipies { get; set; }
}


public static class ModelBuilderExtensions
{
    /// <summary>
    /// Tábla nevének megadása, hogy az entitás neve legyen az
    /// </summary>
    /// <param name="modelBuilder"></param>
    public static void RemovePluralizingTableNameConvention(this ModelBuilder modelBuilder)
    {
        foreach (IMutableEntityType entity in modelBuilder.Model.GetEntityTypes())
        {
            if (entity.BaseType == null && !HasAttribute(entity.ClrType, typeof(TableAttribute)))
            {
                entity.SetTableName(entity.DisplayName());
            }
        }
    }

    /// <summary>
    /// Egy-több kapcsolat esetében a cascade törlés letiltása
    /// </summary>
    /// <param name="modelBuilder"></param>
    public static void RemoveOneToManyCascadeDeleteConvention(this ModelBuilder modelBuilder)
    {
        var cascadeFKs = modelBuilder.Model.GetEntityTypes()
            .SelectMany(t => t.GetForeignKeys())
            .Where(fk => !fk.IsOwnership && fk.DeleteBehavior == DeleteBehavior.Cascade);

        foreach (var fk in cascadeFKs)
            fk.DeleteBehavior = DeleteBehavior.Restrict;
    }

    private static bool HasAttribute(Type type, Type attributeType)
    {
        return type.GetCustomAttribute(attributeType) != null;
    }
}