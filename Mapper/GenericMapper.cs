using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;

namespace EFGetStarted.Mapper;

public abstract class GenericMapper<TEntity, TPostDto, TPutDto, TGetDto>
    where TEntity : AbstractEntity
    where TPostDto : AbstractPostDto
    where TPutDto : AbstractPutDto
    where TGetDto : AbstractGetDto
{
    public abstract TEntity ToEntity(TPostDto dto);

    public abstract TEntity ToEntity(TPutDto dto);

    public abstract TGetDto ToGetDto(TEntity entity);

    public List<TGetDto> ToList(List<TEntity> list)
    {
        return list.Select(ToGetDto).ToList();
    }
}