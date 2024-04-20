using EFGetStarted.Model.Entity;
using EFGetStarted.Repository;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.UnitOfWork
{
    public sealed class UnitOfWork : IUnitOfWork
    {
        private readonly DbContext _recipeCollectionContext;
        private readonly Dictionary<Type, object> _repositories = new();


        public UnitOfWork(RecipeCollectionContext recipeCollectionContext)
        {
            _recipeCollectionContext = recipeCollectionContext;
        }

        public DbContext Context()
        {
            return _recipeCollectionContext;
        }

        public IGenericRepository<TEntity> GetRepository<TEntity>() where TEntity : AbstractEntity
        {
            var type = typeof(TEntity);
            if (!_repositories.ContainsKey(type))
            {
                _repositories[type] = new GenericRepository<TEntity>(_recipeCollectionContext);
            }
            return (IGenericRepository<TEntity>)_repositories[type];
        }

        public int SaveChanges()
        {
            return _recipeCollectionContext.SaveChanges();
        }

        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        private void Dispose(bool disposing)
        {
            if (disposing)
            {
                _recipeCollectionContext.Dispose();
            }
        }

        public DbSet<TEntity> GetDbSet<TEntity>() where TEntity : AbstractEntity
        {
            return _recipeCollectionContext.Set<TEntity>();
        }

        public Task SaveChangesAsync()
        {
            return _recipeCollectionContext.SaveChangesAsync();
        }
    }
}
