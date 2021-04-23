package hu.webuni.hr.gyd.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.gyd.dto.CompanyDto;
import hu.webuni.hr.gyd.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
	
	CompanyDto companyToDto(Company company);
	
	@Mapping(target = "employees", ignore = true)
	@Named("noEmployees")
	CompanyDto companyWOEmployeesToDto(Company company);
	
	List<CompanyDto> companiesToDtos(List<Company> companies);
	
	@IterableMapping(qualifiedByName = "noEmployees")
	List<CompanyDto> companiesWOEmployeesToDtos(List<Company> companies);
	
	Company DtoToCompany(CompanyDto companyDto);

}
