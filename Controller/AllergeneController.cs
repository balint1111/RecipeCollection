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
    public class AllergenController : Microsoft.AspNetCore.Mvc.Controller
    {
        private readonly IAllergenService _allergenService;

        public AllergenController(
            IAllergenService allergenService
        )
        {
            _allergenService = allergenService;
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet("{id}")]
        public async Task<AllergenGetDto> GetById(int id)
        {
            return await _allergenService.GetById(id);
        }
        
        [AllowAnonymous]
        [HttpGet]
        public async Task<List<AllergenGetDto>> GetAll(bool showDeleted)
        {
            return await _allergenService.GetAll(showDeleted);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpPost]
        public async Task AddAllergen(int allergenId)
        {
            await _allergenService.AddAllergen(allergenId);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpDelete]
        public async Task DeleteAllergen(int allergenId)
        {
            await _allergenService.DeleteAllergen(allergenId);
        }

        [Authorize(Roles = Roles.ADMIN)]
        [HttpPost]
        [Transactional]
        public async Task Create(AllergenPostDto allergen)
        {
            await _allergenService.Create(allergen);
        }

        [Authorize(Roles = Roles.ADMIN)]
        [HttpPut]
        [Transactional]
        public async Task Update(AllergenPutDto allergen)
        {
            await _allergenService.Update(allergen);
        }

        [Authorize(Roles = Roles.ADMIN)]
        [HttpDelete("{id}")]
        public async Task Delete(int id)
        {
            await _allergenService.Delete(id);
        }
    }
}