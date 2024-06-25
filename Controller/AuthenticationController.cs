using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using EFGetStarted.Exceptions;
using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services;
using EFGetStarted.Services.Interface;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;

namespace EFGetStarted.Controller
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    public class AuthenticationController : Microsoft.AspNetCore.Mvc.Controller
    {
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly SignInManager<ApplicationUser> _signInManager;
        private readonly IConfiguration _configuration;
        private readonly IUserService _userService;

        public AuthenticationController(
            UserManager<ApplicationUser> userManager,
            SignInManager<ApplicationUser> signInManager,
            IConfiguration configuration,
            IUserService userService)
        {
            _userManager = userManager;
            _signInManager = signInManager;
            _configuration = configuration;
            _userService = userService;
        }
        
        [AllowAnonymous]
        [HttpPost]
        public async Task<IActionResult> RegisterUser([FromBody] UserRegistrationDTO userForRegistration)
        {
            var result = await _userService.CreateUser(userForRegistration, new[] { Roles.RECIPE_READER, Roles.RECIPE_WRITER });
            return result ? StatusCode(201) : throw new ApplicationException("Registration failed!");
        }
        
        [AllowAnonymous]
        [HttpPost]
        public async Task<IActionResult> Login([FromBody] UserLoginDTO userLoginDto)
        {
            var user = await _userManager.FindByNameAsync(userLoginDto.Username);
            if (user != null && await _userManager.CheckPasswordAsync(user, userLoginDto.Password))
            {
                var userRoles = await _userManager.GetRolesAsync(user);

                var authClaims = new List<Claim>
                {
                    new(ClaimTypes.Name, user.UserName),
                    new(ClaimTypes.NameIdentifier, user.Id.ToString()),
                    new(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
                };

                foreach (var userRole in userRoles)
                {
                    authClaims.Add(new Claim(ClaimTypes.Role, userRole));
                }

                var token = GetToken(authClaims);

                return Ok(new
                {
                    token = new JwtSecurityTokenHandler().WriteToken(token),
                    expiration = token.ValidTo
                });
            }
            return Unauthorized();
        }

        private JwtSecurityToken GetToken(List<Claim> authClaims)
        {
            var authSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]!));

            var token = new JwtSecurityToken(
                issuer: _configuration["JWT:ValidIssuer"],
                audience: _configuration["JWT:ValidAudience"],
                expires: DateTime.Now.AddHours(4),
                claims: authClaims,
                signingCredentials: new SigningCredentials(authSigningKey, SecurityAlgorithms.HmacSha256)
            );

            return token;
        }

        [Authorize(Roles = Roles.All)]
        [HttpPost]
        public async Task Logout()
        {
            await _signInManager.SignOutAsync();
        }
        
        [Authorize(Roles = Roles.ADMIN)]
        [HttpPost]
        public async Task<IActionResult> InitRoles()
        {
            await _userService.InitRoles();
            return Ok();
        }
        
        [Authorize(Roles = Roles.ADMIN)]
        [HttpPost]
        public async Task<IActionResult> InitUsers()
        {
            await _userService.InitUsers();
            return Ok();
        }
        
        [Authorize(Roles = Roles.ADMIN)]
        [HttpDelete("{id}")]
        public async Task Delete(int id)
        {
            await _userService.Delete(id);
        }
    }
}