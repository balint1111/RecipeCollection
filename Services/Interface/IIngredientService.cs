using EFGetStarted.Model.DTO;

namespace EFGetStarted.Services.Interface
{
    public interface IIngredientService
    {
        public Task<IngredientGetDto> GetById(int id);
        public Task<List<IngredientGetDto>> GetAll(bool showDeleted);
        public Task Create(IngredientPostDto ingredient);
        public Task Update(IngredientPutDto ingredient);
        public Task Delete(int id);
    }
}
