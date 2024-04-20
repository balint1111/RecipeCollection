using System.Security.Claims;
using EFGetStarted.Model.Entity;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Logging;
using Microsoft.IO;

namespace EFGetStarted.Middleware
{
    public class RequestResponseLoggingMiddleware
    {
        private readonly RequestDelegate _next;
        private readonly ILogger _logger;
        private readonly RecyclableMemoryStreamManager _recyclableMemoryStreamManager;
        private readonly CurrentUser _currentUser;

        public RequestResponseLoggingMiddleware(RequestDelegate next, ILoggerFactory loggerFactory, CurrentUser currentUser)
        {
            _next = next;
            _currentUser = currentUser;
            _logger = loggerFactory.CreateLogger<RequestResponseLoggingMiddleware>();
            _recyclableMemoryStreamManager = new RecyclableMemoryStreamManager();
        }

        public async Task Invoke(HttpContext context, UserManager<ApplicationUser> userManager)
        {
            await LogRequest(context, userManager);
            await _next(context);
        }

        private async Task LogRequest(HttpContext context, UserManager<ApplicationUser> userManager)
        {
            if (context.GetEndpoint()?.Metadata?.GetMetadata<IAllowAnonymous>() is not object)
            {
                context.Request.EnableBuffering();
                await using var requestStream = _recyclableMemoryStreamManager.GetStream();
                await context.Request.Body.CopyToAsync(requestStream);
                var currentUser = await _currentUser.GetCurrentUserAsync();
                if (currentUser != null)
                {
                    _logger.LogInformation($"Http Request information:{Environment.NewLine}" +
                                           $"Method: {context.Request.Method}{Environment.NewLine}" +
                                           $"Endpoint: {context.Request.Path}{Environment.NewLine}" +
                                           $"Body: {await GetResponseBodyContent(context.Request.Body)}{Environment.NewLine}"
                                               .Let(s => currentUser == null ? s : s +
                                                   $"User Id: {currentUser.Id}{Environment.NewLine}" +
                                                   $"Username: {currentUser.UserName}{Environment.NewLine}"
                                               )
                    );
                }

                context.Request.Body.Position = 0;
            }
        }

        private async Task<string> GetResponseBodyContent(Stream requestStream)
        {
            requestStream.Seek(0, SeekOrigin.Begin);

            string bodyText = await new StreamReader(requestStream).ReadToEndAsync();

            requestStream.Seek(0, SeekOrigin.Begin);

            return bodyText;
        }
    }
}