using EFGetStarted.Model.Entity;
using EFGetStarted.Services.Interface;
using EFGetStarted.UnitOfWork;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Caching.Memory;

namespace EFGetStarted.Services
{
    public class CacheService: ICacheService
    {
        private readonly IMemoryCache _cache; 
        private readonly IUnitOfWork _unitOfWork;

        public CacheService(IMemoryCache cache, IUnitOfWork unitOfWork)
        {
            _cache = cache;
            _unitOfWork = unitOfWork;
        }

        public void SetCache()
        {
            _unitOfWork.GetDbSet<Allergen>().AsNoTracking().ToList().ForEach(allergen =>
            {
                _cache.Set(allergen.Id, allergen);
            });
            _unitOfWork.GetDbSet<Material>().AsNoTracking().ToList().ForEach(material =>
            {
                _cache.Set(material.Id, material);
            });
        }

        public bool TryGetValue<TEntity>(object cacheKey, out TEntity? value)
        {
            return _cache.TryGetValue(cacheKey, out value);
        }

        public void Remove(object cacheKey)
        {
            _cache.Remove(cacheKey);
        }

        public void Set(object cacheKey, object value)
        {
            _cache.Set(cacheKey, value);
        }
    }
}
