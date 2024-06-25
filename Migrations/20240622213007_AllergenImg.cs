using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace EFGetStarted.Migrations
{
    /// <inheritdoc />
    public partial class AllergenImg : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserFavorite_Allergen_RecipeId",
                table: "UserFavorite");

            migrationBuilder.AddColumn<string>(
                name: "ImgBase64",
                table: "Allergen",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");

            migrationBuilder.AddForeignKey(
                name: "FK_UserFavorite_Recipe_RecipeId",
                table: "UserFavorite",
                column: "RecipeId",
                principalTable: "Recipe",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserFavorite_Recipe_RecipeId",
                table: "UserFavorite");

            migrationBuilder.DropColumn(
                name: "ImgBase64",
                table: "Allergen");

            migrationBuilder.AddForeignKey(
                name: "FK_UserFavorite_Allergen_RecipeId",
                table: "UserFavorite",
                column: "RecipeId",
                principalTable: "Allergen",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
