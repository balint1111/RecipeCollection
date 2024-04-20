using EFGetStarted.Model.DTO;

namespace EFGetStarted.Services.Interface
{
    public interface IAllergenService
    {
        public Task<AllergenGetDto> GetById(int id);
        public Task<List<AllergenGetDto>> GetAll(bool showDeleted);
        public Task AddAllergen(int allergenId);
        public Task DeleteAllergen(int allergenId);
        public Task Create(AllergenPostDto allergen);
        public Task Update(AllergenPutDto allergen);
        public Task Delete(int id);
    }
}
