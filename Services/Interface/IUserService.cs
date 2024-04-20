using EFGetStarted.Model.DTO;
using Microsoft.AspNetCore.Identity;

namespace EFGetStarted.Services.Interface
{
    public interface IUserService
    {
        public Task InitRoles();
        public Task InitUsers();

        public Task CreateAdminIfNotExist();
        public Task<bool> CreateUser(UserRegistrationDTO registrationDto, string[] roles);
        public Task<IdentityResult> Delete(int id);
    }
}