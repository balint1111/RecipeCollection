using EFGetStarted.Model.Entity;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;

namespace EFGetStarted
{
    public class CurrentUser
    {
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IHttpContextAccessor _httpContextAccessor;

        public CurrentUser(
            UserManager<ApplicationUser> userManager, IHttpContextAccessor httpContextAccessor)
        {
            _userManager = userManager;
            _httpContextAccessor = httpContextAccessor;
        }

        public async Task<ApplicationUser?> GetCurrentUserAsync()  {
            var asd = await _userManager.GetUserAsync(_httpContextAccessor.HttpContext!.User);
            return asd;
        }
            
        
        public int? UserId()
        {
            var userIdStr = _userManager.GetUserId(_httpContextAccessor.HttpContext!.User);
            if (userIdStr == null)
            {
                return null;
            }
            return int.Parse(userIdStr);
        }

        public async Task<int?> UserIdAsync() => (await GetCurrentUserAsync())?.Id;
    }
}