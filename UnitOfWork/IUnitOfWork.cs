using EFGetStarted.Model.Entity;
using EFGetStarted.Repository;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.UnitOfWork
{
    public interface IUnitOfWork: IDisposable
    {
        IGenericRepository<TEntity> GetRepository<TEntity>() where TEntity : AbstractEntity;
        DbSet<TEntity> GetDbSet<TEntity>() where TEntity : AbstractEntity;
        int SaveChanges();
        Task SaveChangesAsync();
        DbContext Context();
    }
}
