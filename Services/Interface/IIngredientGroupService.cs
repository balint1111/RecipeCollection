using EFGetStarted.Model.DTO;

namespace EFGetStarted.Services.Interface
{
    public interface IIngredientGroupService
    {
        public Task<IngredientGroupGetDto> GetById(int id);

        public Task<List<IngredientGroupGetDto>> GetAll(bool showDeleted);
        public Task Create(IngredientGroupPostDto ingredientGroup);
        public Task Update(IngredientGroupPutDto ingredientGroup);
        public Task Delete(int id);
    }
}
