package hu.webuni.hr.gyd.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.gyd.dto.CompanyDto;
import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.CompanyType;

@Mapper(componentModel = "spring", uses = EmployeeMapper.class)
public interface CompanyMapper {
	
	@Mapping(target = "companyType", source = "companyType.name")
	CompanyDto companyToDto(Company company);
	
	@Mapping(target = "employees", ignore = true)
	@Mapping(target = "companyType", source = "companyType.name")
	@Named("noEmployees")
	CompanyDto companyWOEmployeesToDto(Company company);
	
	List<CompanyDto> companiesToDtos(List<Company> companies);
	
	@IterableMapping(qualifiedByName = "noEmployees")
	List<CompanyDto> companiesWOEmployeesToDtos(List<Company> companies);
	
	@Named("stringToCompanyType")
	public static CompanyType stringToCompanyType(String type) {
		return new CompanyType(1L, type);
	}
	
	@Mapping(source = "companyType", target = "companyType", qualifiedByName = "stringToCompanyType")
	@Mapping(target = "position", ignore = true)
	Company DtoToCompany(CompanyDto companyDto);

}
