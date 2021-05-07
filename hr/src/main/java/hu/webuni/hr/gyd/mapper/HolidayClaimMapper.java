package hu.webuni.hr.gyd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HolidayClaim;

@Mapper(componentModel = "spring")
public interface HolidayClaimMapper {

	List<HolidayClaimDto> claimsToDtos(List<HolidayClaim> all);
	
	@Mapping(target = "claimant", source = "claimant.name")
	@Mapping(target = "principal", source = "principal.name")
	@Mapping(target = "claimNumber", source = "id")
	HolidayClaimDto claimToDto(HolidayClaim claim);

	@Mapping(source = "claimant", target = "claimant", qualifiedByName = "stringToClaimant")
	@Mapping(source = "principal", target = "principal", qualifiedByName = "stringToPrincipal")
	@Mapping(target = "id", ignore = true)
	HolidayClaim dtoToClaim(HolidayClaimDto claim);

	@Named("stringToClaimant")
	public static Employee stringToClaimant(String name) {
		Employee claimant = new Employee();
		claimant.setName(name);
		return claimant;
	}
	
	@Named("stringToPrincipal")
	public static Employee stringToPrincipal(String name) {
		Employee principal = new Employee();
		principal.setName(name);
		return principal;
	}
	
}
