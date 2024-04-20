using EFGetStarted.Model.Entity;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.Repository
{
    public class GenericRepository<TEntity> : IGenericRepository<TEntity> where TEntity : AbstractEntity
    {
        protected readonly DbSet<TEntity> DbSet;

        public GenericRepository(DbContext recipeCollectionContext)
        {
            DbSet = recipeCollectionContext.Set<TEntity>();
        }

        public IQueryable<TEntity> GetAll()
        {
            return DbSet;
        }

        public async Task<TEntity?> GetById(int id)
        {
            return await DbSet.FirstOrDefaultAsync(e => e.Id == id);
        }

        public async Task<TEntity> Create(TEntity entity)
        {
            var createdEntity = await DbSet.AddAsync(entity);
            return createdEntity.Entity;
        }

        public void Update(TEntity entity)
        {
            DbSet.Update(entity);
        }

        public async Task Delete(int id)
        {
            var entity = await GetById(id) ?? throw new InvalidOperationException();
            DbSet.Remove(entity);
        }

        public async Task<TEntity?> DeleteSoft(int id)
        {
            var entity = await GetById(id) ?? throw new InvalidOperationException();
            entity.Deleted = true;
            Update(entity);
            return entity;
        }
    }
}