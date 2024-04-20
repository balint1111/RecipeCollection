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
    public class IngredientController : Microsoft.AspNetCore.Mvc.Controller
    {
        private readonly IIngredientService _ingredientService;

        public IngredientController(
            IIngredientService ingredientService
        )
        {
            _ingredientService = ingredientService;
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet("{id}")]
        public async Task<IngredientGetDto> GetById(int id)
        {
            return await _ingredientService.GetById(id);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet]
        public async Task<List<IngredientGetDto>> GetAll(bool showDeleted)
        {
            return await _ingredientService.GetAll(showDeleted);
        }

        [Authorize(Roles = $"{Roles.ADMIN},{Roles.RECIPE_WRITER}")]
        [HttpPost]
        [Transactional]
        public async Task Create(IngredientPostDto ingredient)
        {
            await _ingredientService.Create(ingredient);
        }

        [Authorize(Roles = $"{Roles.ADMIN},{Roles.RECIPE_WRITER}")]
        [HttpPut]
        [Transactional]
        public async Task Update(IngredientPutDto ingredient)
        {
            await _ingredientService.Update(ingredient);
        }

        [Authorize(Roles = $"{Roles.ADMIN},{Roles.RECIPE_WRITER}")]
        [HttpDelete("{id}")]
        public async Task Delete(int id)
        {
            await _ingredientService.Delete(id);
        }
    }
}