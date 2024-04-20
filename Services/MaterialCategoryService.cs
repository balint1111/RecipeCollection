using EFGetStarted.Exceptions;
using EFGetStarted.Mapper;
using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services.Interface;
using EFGetStarted.UnitOfWork;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.Services
{
    public class MaterialCategoryService: IMaterialCategoryService
    {
        private readonly IUnitOfWork _unitOfWork;
        private readonly MaterialCategoryMapper _materialCategoryMapper;
        
        public MaterialCategoryService(IUnitOfWork unitOfWork, MaterialCategoryMapper materialCategoryMapper)
        {
            _unitOfWork = unitOfWork;
            _materialCategoryMapper = materialCategoryMapper;
        }
        
        public async Task<MaterialCategoryGetDto> GetById(int id)
        {
            var materialCategory = await _unitOfWork.GetRepository<MaterialCategory>().GetAll()
                .FirstOrDefaultAsync(it => it.Id == id);
            if (materialCategory == null)
                throw new BadRequestException("There is no materialCategory with this id " + id + "!");
            return _materialCategoryMapper.ToGetDto(materialCategory);
        }
        
        public async Task<List<MaterialCategoryGetDto>> GetAll(bool showDeleted)
        {
            var materialCategories = await _unitOfWork.GetRepository<MaterialCategory>().GetAll()
                .Let( it => showDeleted ? it.IgnoreQueryFilters() : it)
                .ToListAsync();
            return _materialCategoryMapper.ToList(materialCategories);
        }
        
        public async Task Create(MaterialCategoryPostDto materialCategory)
        {
            await _unitOfWork.GetRepository<MaterialCategory>().Create(_materialCategoryMapper.ToEntity(materialCategory));
            await _unitOfWork.SaveChangesAsync();
        }
        
        public async Task Update(MaterialCategoryPutDto materialCategory)
        {
            _unitOfWork.GetRepository<MaterialCategory>().Update(_materialCategoryMapper.ToEntity(materialCategory));
            await _unitOfWork.SaveChangesAsync();
        }
        
        public async Task Delete(int id)
        {
            await _unitOfWork.GetRepository<MaterialCategory>().DeleteSoft(id);
            await _unitOfWork.SaveChangesAsync();
        }
    }
}
