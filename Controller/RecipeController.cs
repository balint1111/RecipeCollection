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
    public class RecipeController : Microsoft.AspNetCore.Mvc.Controller
    {
        private readonly IRecipeService _recipeService;

        public RecipeController(
            IRecipeService recipeService
        )
        {
            _recipeService = recipeService;
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet("{id}")]
        public async Task<RecipeGetDto> GetById(int id)
        {
            return await _recipeService.GetById(id);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpPost]
        public async Task AddFavorite(int recipeId)
        {
            await _recipeService.AddFavorite(recipeId);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpDelete]
        public async Task DeleteFavorite(int recipeId)
        {
            await _recipeService.DeleteFavorite(recipeId);
        }
        
        [AllowAnonymous]
        [HttpGet]
        public async Task<List<RecipeGetDto>> GetAll(bool showDeleted)
        {
            return await _recipeService.GetAll(showDeleted);
        }
        
        [Authorize(Roles = Roles.All)]
        [HttpGet]
        public async Task<List<RecipeGetDto>> GetByMaterialId(int materialId, bool showDeleted = true)
        {
            return await _recipeService.GetByMaterialId(materialId, showDeleted);
        }

        [Authorize(Roles = $"{Roles.ADMIN},{Roles.RECIPE_WRITER}")]
        [HttpPost]
        [Transactional]
        public async Task Create(RecipePostDto recipe)
        {
            await _recipeService.Create(recipe);
        }

        [Authorize(Roles = $"{Roles.ADMIN},{Roles.RECIPE_WRITER}")]
        [HttpPut]
        [Transactional]
        public async Task Update(RecipePutDto recipe)
        {
            await _recipeService.Update(recipe);
        }

        [Authorize(Roles = Roles.ADMIN)]
        [HttpDelete("{id}")]
        public async Task Delete(int id)
        {
            await _recipeService.Delete(id);
        }
    }
}