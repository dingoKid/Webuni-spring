package hu.webuni.hr.gyd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.model.HolidayClaim;

@Mapper(componentModel = "spring")
public interface HolidayClaimMapper {

	List<HolidayClaimDto> claimsToDtos(List<HolidayClaim> all);
	
	@Mapping(target = "claimant", source = "claimant.name")
	@Mapping(target = "principal", source = "principal.name")
	HolidayClaimDto claimToDto(HolidayClaim claim);

	
}
