using System.Net;
using EFGetStarted.Exceptions;
using EFGetStarted.Model.DTO;
using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;

namespace EFGetStarted.Middleware
{
    public class RequestResponseMiddleware
    {
        private readonly RequestDelegate _next;

        public RequestResponseMiddleware(RequestDelegate next)
        {
            _next = next;
        }

        public async Task InvokeAsync(HttpContext context)
        {
            SetRequestId(context);

            var originBody = context.Response.Body;
            try
            {
                await SetReponseBodyForSuccess(context, originBody);
            }
            catch (Exception ex)
            {
                await SetResponseBodyForException(context, ex, originBody);
            }
        }

        private void SetRequestId(HttpContext context)
        {
            context.Request.Headers.Append("ID", Guid.NewGuid().ToString());
        }

        private async Task SetReponseBodyForSuccess(HttpContext context, Stream originBody)
        {
            //Assign new stream for Response
            var newResponseStream = new MemoryStream();
            context.Response.Body = newResponseStream;
            await _next(context);

            //Read current response
            newResponseStream.Position = 0;
            var responseBody = new StreamReader(newResponseStream).ReadToEnd();
            var statusCode = context.Response.StatusCode;
            var identity = context.Request.Headers["ID"];
            var serializedRequestBody = JsonConvert.DeserializeObject(responseBody);
            var requestBody = new MiddlewareReturnDTO(statusCode.ToString(), serializedRequestBody, identity);

            //Change Response Body
            var modifiedResponseStream = new MemoryStream();
            var sw = new StreamWriter(modifiedResponseStream);
            sw.Write(JsonConvert.SerializeObject(requestBody));
            sw.Flush();
            modifiedResponseStream.Position = 0;
            context.Response.Headers.ContentType = "application/json; charset=utf-8";
            await modifiedResponseStream.CopyToAsync(originBody);
        }

        private async Task SetResponseBodyForException(HttpContext context, Exception ex, Stream originBody)
        {
            var statusCode = HttpStatusCode.InternalServerError;
            if (ex is BadRequestException)
            {
                statusCode = HttpStatusCode.BadRequest;
            }

            context.Response.StatusCode = (int)statusCode;
            context.Response.Headers.ContentType = "application/json; charset=utf-8";

            var identity = context.Request.Headers["ID"];
            var responseBody = new MiddlewareReturnDTO(context.Response.StatusCode.ToString(), ex.Message, identity);

            var modifiedResponseStream = new MemoryStream();
            var sw = new StreamWriter(modifiedResponseStream);
            sw.Write(JsonConvert.SerializeObject(responseBody));
            sw.Flush();
            modifiedResponseStream.Position = 0;

            await modifiedResponseStream.CopyToAsync(originBody).ConfigureAwait(false);
        }
    }
}