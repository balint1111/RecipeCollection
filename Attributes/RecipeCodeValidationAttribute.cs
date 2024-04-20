using System.ComponentModel.DataAnnotations;

namespace EFGetStarted.Attributes
{
    public class RecipeCodeValidationAttribute: ValidationAttribute
    {

        protected override ValidationResult IsValid(object? value, ValidationContext validationContext)
        {
            if (value is string recipeCode)
            {
                if(recipeCode.Length < 6) return new ValidationResult("recipeCode is less than 6");
                if(recipeCode.Length > 12) return new ValidationResult("recipeCode is more than 12");
                if(char.IsDigit(recipeCode[0])) return new ValidationResult("recipeCode cannot start with a number!");
                foreach (var c in recipeCode)
                {
                    if (!(char.IsDigit(c) || char.IsUpper(c) || c == '_' || c == '!'))
                    {
                        return new ValidationResult("recipeCode should contain only (numbers, uppercase letters, _, !)!");
                    }
                }
                return ValidationResult.Success!;
            }

            return new ValidationResult("Wrong recipeCode!");
        }
    }
}
