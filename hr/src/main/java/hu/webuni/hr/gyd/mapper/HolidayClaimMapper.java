package hu.webuni.hr.gyd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HolidayClaim;

@Mapper(componentModel = "spring")
public interface HolidayClaimMapper {

	@Mapping(source = "principal", target = "principal", qualifiedByName = "stringToEmployee")
	//HolidayClaim DtoToClaim(HolidayClaimDto holidayClaimDto);
	
	@Named("stringToEmployee")
	public static Employee stringToEmployee(String name) {
		Employee principal = new Employee();
		principal.setName(name);
		return principal;
	}

	
}
