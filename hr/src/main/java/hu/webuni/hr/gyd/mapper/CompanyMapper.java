package hu.webuni.hr.gyd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.gyd.dto.CompanyDto;
import hu.webuni.hr.gyd.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
	
	List<CompanyDto> companiesToDtos(List<Company> companies);

	CompanyDto companyToDto(Company company);

	Company DtoToCompany(CompanyDto company);

}
