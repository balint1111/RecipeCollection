using System.Text;
using EFGetStarted;
using EFGetStarted.Attributes;
using EFGetStarted.Mapper;
using EFGetStarted.Middleware;
using EFGetStarted.Model.Entity;
using EFGetStarted.Services;
using EFGetStarted.Services.Interface;
using EFGetStarted.UnitOfWork;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;

var builder = WebApplication.CreateBuilder(args);
ConfigurationManager configuration = builder.Configuration;
builder.Services.AddRazorPages();
builder.Services.AddScoped<ICacheService, CacheService>();
builder.Services.AddScoped<IUserService, UserService>();
builder.Services.AddScoped<IUnitOfWork, UnitOfWork>();
builder.Services.AddScoped<IAllergenService, AllergenService>();
builder.Services.AddScoped<IMaterialCategoryService, MaterialCategoryService>();
builder.Services.AddScoped<IMaterialService, MaterialService>();
builder.Services.AddScoped<IIngredientService, IngredientService>();
builder.Services.AddScoped<IIngredientGroupService, IngredientGroupService>();
builder.Services.AddScoped<IRecipeService, RecipeService>();
builder.Services.AddScoped<AllergenMapper>();
builder.Services.AddScoped<MaterialCategoryMapper>();
builder.Services.AddScoped<MaterialMapper>();
builder.Services.AddScoped<IngredientMapper>();
builder.Services.AddScoped<IngredientGroupMapper>();
builder.Services.AddScoped<RecipeMapper>();
builder.Services.AddScoped<CurrentUser>();
builder.Services.AddHttpContextAccessor();

builder.Services.AddIdentity<ApplicationUser, IdentityRole<int>>(options =>
{
    options.User.RequireUniqueEmail = true;
})
    .AddEntityFrameworkStores<RecipeCollectionContext>()
    .AddDefaultTokenProviders();

#region Db
var connectionString = configuration.GetConnectionString("DefaultConnection");
builder.Services.AddDbContext<RecipeCollectionContext>(options =>
{
    var dbBuilder = options.UseSqlServer(connectionString);
    if (builder.Environment.IsDevelopment())
    {
        dbBuilder.EnableSensitiveDataLogging();
    }
});

#endregion



builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme).AddJwtBearer(options =>
{
    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuer = true,
        ValidateAudience = true,
        ValidAudience = configuration["JWT:ValidAudience"],
        ValidIssuer = configuration["JWT:ValidIssuer"],
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(configuration["JWT:Secret"]!))
    };
});
builder.Services.AddControllers();
// Add swagger api doc configuration and JWT authorization for swagger 
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "RecipeCollection", Version = "v1" });
    c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme()
    {
        Name = "Authorization",
        Type = SecuritySchemeType.Http,
        Scheme = "Bearer",
        BearerFormat = "JWT",
        In = ParameterLocation.Header,
        Description = "JWT Authorization header using the Bearer scheme."

    });
    c.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference
                {
                    Type = ReferenceType.SecurityScheme,
                    Id = "Bearer"
                }
            },
            new string[] {}
        }
    });
});





var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseSwagger();
app.UseSwaggerUI(options =>
{
    options.SwaggerEndpoint("/swagger/v1/swagger.json", "v1");
});

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();

app.UseMiddleware<RequestResponseLoggingMiddleware>();
app.UseMiddleware<RequestResponseMiddleware>();

app.MapRazorPages();
app.UseEndpoints(endpoints =>
{
    endpoints.MapControllers();
});
using (var scope = app.Services.CreateScope())
{
    var cache = scope.ServiceProvider.GetService<ICacheService>();
    if (cache != null)
    {
        cache.SetCache();
    }
}
await app.Services.GetRequiredService<IUserService>().CreateAdminIfNotExist();
app.Run();
