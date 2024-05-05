using EFGetStarted.Attributes;
using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services.Interface;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace EFGetStarted.Controller
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    public class MaterialCategoryController : Microsoft.AspNetCore.Mvc.Controller
    {
        private readonly IMaterialCategoryService _materialCategoryService;

        public MaterialCategoryController(
            IMaterialCategoryService materialCategoryService
        )
        {
            _materialCategoryService = materialCategoryService;
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet("{id}")]
        public async Task<MaterialCategoryGetDto> GetById(int id)
        {
            return await _materialCategoryService.GetById(id);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet]
        public async Task<List<MaterialCategoryGetDto>> GetAll(bool showDeleted)
        {
            return await _materialCategoryService.GetAll(showDeleted);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet]
        public async Task<PageResponseDto<MaterialCategoryGetDto>> GetAllPageable(
            bool showDeleted,
            int page = 1,
            int pageSize = 10,
            string? filter = ""
            )
        {
            return await _materialCategoryService.GetAllPageable(showDeleted, new PageableDto(page, pageSize, filter ?? ""));
        }
        

        [Authorize(Roles = Roles.ADMIN)]
        [HttpPost]
        [Transactional]
        public async Task Create(MaterialCategoryPostDto materialCategory)
        {
            await _materialCategoryService.Create(materialCategory);
        }

        [Authorize(Roles = Roles.ADMIN)]
        [HttpPut]
        [Transactional]
        public async Task Update(MaterialCategoryPutDto materialCategory)
        {
            await _materialCategoryService.Update(materialCategory);
        }

        [Authorize(Roles = Roles.ADMIN)]
        [HttpDelete("{id}")]
        public async Task Delete(int id)
        {
            await _materialCategoryService.Delete(id);
        }
    }
}