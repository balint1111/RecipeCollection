using EFGetStarted.Model.DTO;
using EFGetStarted.Model.Entity;
using EFGetStarted.Repository;
using EFGetStarted.UnitOfWork;

namespace EFGetStarted.Mapper;

public class AllergenMapper : GenericMapper<Allergen, AllergenPostDto, AllergenPutDto, AllergenGetDto>
{
    private readonly IGenericRepository<UserAllergen> _userAllergenRepository;
    private readonly CurrentUser _currentUser;

    public AllergenMapper(IUnitOfWork unitOfWork, CurrentUser currentUser)
    {
        _currentUser = currentUser;
        _userAllergenRepository = unitOfWork.GetRepository<UserAllergen>();
    }

    public override Allergen ToEntity(AllergenPostDto dto)
    {
        return new Allergen
        {
            Name = dto.Name
        };
    }

    public override Allergen ToEntity(AllergenPutDto dto)
    {
        return new Allergen
        {
            Name = dto.Name
        }.Let( it =>
        {
            if (dto.Id != null) it.Id = (int)dto.Id;
            return it;
        });
    }

    public override AllergenGetDto ToGetDto(Allergen entity)
    {
        var userId = _currentUser.UserId();
        return new AllergenGetDto()
        {
            Id = entity.Id,
            Name = entity.Name,
            IsUserAllergen = _userAllergenRepository.GetAll()
                .Where(it => it.UserId == userId && it.AllergenId == entity.Id)
                .ToList().Count != 0
        };
    }

    public UserAllergen AllergenIdAndUserIdToUserAllergen(int allergenId, int userId)
    {
        return new UserAllergen()
        {
            AllergenId = allergenId,
            UserId = userId
        };
    }
}