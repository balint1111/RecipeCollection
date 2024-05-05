using EFGetStarted.Mapper;
using EFGetStarted.Model.Entity;
using Microsoft.EntityFrameworkCore;

namespace EFGetStarted.Model.DTO;

public class PageableDto
{
    public PageableDto(int page = 1, int pageSize = 10, string filter = "")
    {
        Page = page;
        PageSize = pageSize;
        Filter = filter;
    }

    public int Page { get; set; }
    public int PageSize { get; set; }
    public string Filter { get; set; }

    public async Task<PageResponseDto<TGetDto>> ToPage<TEntity, TPostDto, TPutDto, TGetDto>(
        IQueryable<TEntity> query, GenericMapper<TEntity, TPostDto, TPutDto, TGetDto> mapper
        )
        where TEntity : AbstractEntity
        where TPostDto : AbstractPostDto
        where TPutDto : AbstractPutDto
        where TGetDto : AbstractGetDto
    {
        var totalCount = query.Count();
        var totalPages = (int)Math.Ceiling((double)totalCount / PageSize);
        query = query.Skip((Page - 1) * PageSize).Take(PageSize);
        return new PageResponseDto<TGetDto>(
            totalCount,
            totalPages,
            Page,
            PageSize,
            mapper.ToList(await query.ToListAsync())
            );
    }
}