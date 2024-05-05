namespace EFGetStarted.Model.DTO;


public class PageResponseDto<T>
{
    public PageResponseDto(int totalCount, int totalPages, int currentPage, int pageSize, List<T> content)
    {
        TotalCount = totalCount;
        TotalPages = totalPages;
        CurrentPage = currentPage;
        PageSize = pageSize;
        Content = content;
    }

    public int TotalCount { get; set; }
    public int TotalPages { get; set; }
    public int CurrentPage { get; set; }
    public int PageSize { get; set; }
    public List<T> Content { get; set; }
    
}