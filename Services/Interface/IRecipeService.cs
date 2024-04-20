﻿using EFGetStarted.Model.DTO;

namespace EFGetStarted.Services.Interface
{
    public interface IRecipeService
    {
        public Task<RecipeGetDto> GetById(int id);

        public Task<List<RecipeGetDto>> GetAll(bool showDeleted);
        public Task<List<RecipeGetDto>> GetByMaterialId(int materialId, bool showDeleted);

        public Task AddFavorite(int recipeId);

        public Task DeleteFavorite(int recipeId);
        public Task Create(RecipePostDto recipe);
        public Task Update(RecipePutDto recipe);
        public Task Delete(int id);
    }
}