using System.Security.Claims;
using EFGetStarted.Exceptions;
using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services.Interface;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.Services
{
    public class UserService : IUserService
    {
        private readonly RoleManager<IdentityRole<int>> _roleManager;
        private readonly UserManager<ApplicationUser> _userManager;

        public UserService(RoleManager<IdentityRole<int>> roleManager,
            UserManager<ApplicationUser> userManager)
        {
            _userManager = userManager;
            _roleManager = roleManager;
        }

        public async Task InitRoles()
        {
            await CreateRole(Roles.ADMIN);
            await CreateRole(Roles.RECIPE_READER);
            await CreateRole(Roles.RECIPE_WRITER);
        }

        public async Task InitUsers()
        {
            await CreateUser(
                new UserRegistrationDTO("admin@test.com", "Password_123", "admin", "admin", "Veszprém", "Magyarország"),
                new[] { Roles.ADMIN });
            await CreateUser(
                new UserRegistrationDTO("writer@test.com", "Password_123", "writer", "writer", "Veszprém",
                    "Magyarország"), new[] { Roles.RECIPE_WRITER });
            await CreateUser(
                new UserRegistrationDTO("reader@test.com", "Password_123", "reader", "reader", "Veszprém",
                    "Magyarország"), new[] { Roles.RECIPE_READER });
        }

        public async Task CreateAdminIfNotExist()
        {
            var admins = await _userManager.GetUsersForClaimAsync(new Claim(ClaimTypes.Role, Roles.ADMIN));
            if (admins.Count == 0)
            {
                await CreateUser(
                    new UserRegistrationDTO("admin@test.com", "Password_123", "admin", "admin", "Veszprém",
                        "Magyarország"),
                    new[] { Roles.ADMIN });
            }
        }

        public async Task<bool> CreateUser(UserRegistrationDTO registrationDto, string[] roles)
        {
            ApplicationUser admin = new ApplicationUser()
            {
                Email = registrationDto.Email,
                UserName = registrationDto.UserName,
                EmailConfirmed = true,
                Name = registrationDto.Name,
                Country = registrationDto.Country,
                Settlement = registrationDto.Settlement,
                Deleted = false
            };
            var user = await _userManager.CreateAsync(admin, registrationDto.Password);
            if (user.Errors.Count() != 0) throw new BadRequestException(String.Join(", ", user.Errors.Select(it => it.Description)));
            await _userManager.AddToRolesAsync(admin, roles);
            await _userManager.AddClaimsAsync(admin, roles.Select(it => new Claim(ClaimTypes.Role, it)));
            return true;
        }

        private async Task CreateRole(string roleName)
        {
            var role = await _roleManager.FindByNameAsync(roleName);
            if (role == null)
            {
                await _roleManager.CreateAsync(new IdentityRole<int>(roleName));
            }
        }

        public async Task<IdentityResult> Delete(int id)
        {
            var user = await _userManager.FindByIdAsync(id.ToString());
            user.Deleted = true;
            return await _userManager.UpdateAsync(user);
        }
    }
}