using EFGetStarted.Model.DTO;

namespace EFGetStarted.Services.Interface
{
    public interface IMaterialCategoryService
    {
        public Task<MaterialCategoryGetDto> GetById(int id);
        public Task<List<MaterialCategoryGetDto>> GetAll(bool showDeleted);
        public Task<PageResponseDto<MaterialCategoryGetDto>> GetAllPageable(bool showDeleted, PageableDto pageable);
        public Task Create(MaterialCategoryPostDto materialCategory);
        public Task Update(MaterialCategoryPutDto materialCategory);
        public Task Delete(int id);
    }
}
