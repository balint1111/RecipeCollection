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
    public class MaterialController : Microsoft.AspNetCore.Mvc.Controller
    {
        private readonly IMaterialService _materialService;

        public MaterialController(
            IMaterialService materialService
        )
        {
            _materialService = materialService;
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet("{id}")]
        public async Task<MaterialGetDto> GetById(int id)
        {
            return await _materialService.GetById(id);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet]
        public async Task<List<MaterialGetDto>> GetAll(bool showDeleted)
        {
            return await _materialService.GetAll(showDeleted);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet]
        public async Task<List<MaterialGetDto>> GetAllByCategory(int materialCategoryId, bool showDeleted)
        {
            return await _materialService.GetAllByCategory(materialCategoryId, showDeleted);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet]
        public async Task<List<MaterialGetDto>> GetAllByAllergens(List<int> allergenIds, bool showDeleted)
        {
            return await _materialService.GetAllByAllergens(allergenIds, showDeleted);
        }

        [Authorize(Roles = Roles.ADMIN)]
        [HttpPost]
        [Transactional]
        public async Task Create(MaterialPostDto material)
        {
            await _materialService.Create(material);
        }

        [Authorize(Roles = Roles.ADMIN)]
        [HttpPut]
        [Transactional]
        public async Task Update(MaterialPutDto material)
        {
            await _materialService.Update(material);
        }

        [Authorize(Roles = Roles.ADMIN)]
        [HttpDelete("{id}")]
        public async Task Delete(int id)
        {
            await _materialService.Delete(id);
        }
    }
}