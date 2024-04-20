namespace EFGetStarted.Model.DTO
{
    public class UserRegistrationDTO
    {
        public UserRegistrationDTO(string email, string password, string userName, string name, string settlement, string country)
        {
            Email = email;
            Password = password;
            UserName = userName;
            Name = name;
            Settlement = settlement;
            Country = country;
        }

        public string Email { get; set; }
        public string Password { get; set; }
        public string UserName { get; set; }
        public string Name { get; set; }
        public string Settlement { get; set; }
        public string Country { get; set; }
    }
}
