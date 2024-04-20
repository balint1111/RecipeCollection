namespace EFGetStarted.Model.DTO
{
    public static class Roles
    {
        public const string ADMIN = "Admin";
        public const string RECIPE_WRITER = "RecipeWriter";
        public const string RECIPE_READER = "RecipeReader";
        public const string All = ADMIN + "," + RECIPE_WRITER + "," + RECIPE_READER;
    }
}
