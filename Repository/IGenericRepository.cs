using Microsoft.EntityFrameworkCore.ChangeTracking;

namespace EFGetStarted.Repository
{
    public interface IGenericRepository<TEntity> where TEntity : class
    {
        IQueryable<TEntity> GetAll();
        Task<TEntity?> GetById(int id);
        Task<TEntity> Create(TEntity entity);
        public Task<TEntity[]> CreateAll(List<TEntity> entities);
        void Update(TEntity entity);
        Task Delete(int id);
        Task<TEntity?> DeleteSoft(int id);
    }
}
