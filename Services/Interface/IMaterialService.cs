using EFGetStarted.Model.DTO;

namespace EFGetStarted.Services.Interface
{
    public interface IMaterialService
    {
        public Task<MaterialGetDto> GetById(int id);
        public Task<List<MaterialGetDto>> GetAll(bool showDeleted);
        public Task<PageResponseDto<MaterialGetDto>> GetAllPageable(bool showDeleted, PageableDto pageable);
        public Task<List<MaterialGetDto>> GetAllByCategory(int materialCategoryId, bool showDeleted);
        public Task<List<MaterialGetDto>> GetAllByAllergens(List<int> allergenIds, bool showDeleted);
        public Task Create(MaterialPostDto material);
        public Task Update(MaterialPutDto material);
        public Task Delete(int id);
    }
}