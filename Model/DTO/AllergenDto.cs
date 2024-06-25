namespace EFGetStarted.Model.DTO;

public class AllergenPostDto : AbstractPostDto
{
    public string Name { get; set; }
    public string ImgBase64 { get; set; }
}

public class AllergenPutDto : AbstractPutDto
{
    public string Name { get; set; }
    public string ImgBase64 { get; set; }
}

public class AllergenGetDto : AbstractGetDto
{
    public string Name { get; set; }
    public string ImgBase64 { get; set; }
    public bool IsUserAllergen { get; set; }
}