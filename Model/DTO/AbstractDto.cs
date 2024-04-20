namespace EFGetStarted.Model.DTO;


public abstract class AbstractPostDto
{
}
public abstract class AbstractPutDto : AbstractDtoWithId
{
}

public abstract class AbstractGetDto : AbstractDtoWithId
{
}
public abstract class AbstractDtoWithId
{
    public int Id { get; set; }
}