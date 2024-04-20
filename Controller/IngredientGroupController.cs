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
    public class IngredientGroupController : Microsoft.AspNetCore.Mvc.Controller
    {
        private readonly IIngredientGroupService _ingredientGroupService;

        public IngredientGroupController(
            IIngredientGroupService ingredientGroupService
        )
        {
            _ingredientGroupService = ingredientGroupService;
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet("{id}")]
        public async Task<IngredientGroupGetDto> GetById(int id)
        {
            return await _ingredientGroupService.GetById(id);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet]
        public async Task<List<IngredientGroupGetDto>> GetAll(bool showDeleted)
        {
            return await _ingredientGroupService.GetAll(showDeleted);
        }

        [Authorize(Roles = $"{Roles.ADMIN},{Roles.RECIPE_WRITER}")]
        [HttpPost]
        [Transactional]
        public async Task Create(IngredientGroupPostDto ingredientGroup)
        {
            await _ingredientGroupService.Create(ingredientGroup);
        }

        [Authorize(Roles = $"{Roles.ADMIN},{Roles.RECIPE_WRITER}")]
        [HttpPut]
        [Transactional]
        public async Task Update(IngredientGroupPutDto ingredientGroup)
        {
            await _ingredientGroupService.Update(ingredientGroup);
        }

        [Authorize(Roles = $"{Roles.ADMIN},{Roles.RECIPE_WRITER}")]
        [HttpDelete("{id}")]
        public async Task Delete(int id)
        {
            await _ingredientGroupService.Delete(id);
        }
    }
}