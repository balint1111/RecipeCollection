using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace EFGetStarted.Migrations
{
    /// <inheritdoc />
    public partial class CreatedByToRecipe : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "CreatedByUserId",
                table: "Recipe",
                type: "int",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.CreateIndex(
                name: "IX_Recipe_CreatedByUserId",
                table: "Recipe",
                column: "CreatedByUserId");

            migrationBuilder.AddForeignKey(
                name: "FK_Recipe_AspNetUsers_CreatedByUserId",
                table: "Recipe",
                column: "CreatedByUserId",
                principalTable: "AspNetUsers",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Recipe_AspNetUsers_CreatedByUserId",
                table: "Recipe");

            migrationBuilder.DropIndex(
                name: "IX_Recipe_CreatedByUserId",
                table: "Recipe");

            migrationBuilder.DropColumn(
                name: "CreatedByUserId",
                table: "Recipe");
        }
    }
}
