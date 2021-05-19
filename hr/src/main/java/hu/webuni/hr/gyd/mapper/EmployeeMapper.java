package hu.webuni.hr.gyd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.gyd.dto.EmployeeDto;
import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Position;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	List<EmployeeDto> employeesToDtos(List<Employee> emloyees);
	
	@Named("stringToPosition")
	public static Position stringToPosition(String positionName) {
		Position position = new Position();
		position.setName(positionName);
		return position;
	}
	
	@Named("stringToCompany")
	public static Company stringToCompany(String companyName) {
		Company company = new Company();
		company.setName(companyName);
		return company;
	}
	
	@Mapping(source = "position.name", target = "position")
	@Mapping(source = "company.name", target = "company")
	EmployeeDto employeeToDto(Employee employee);

	@Mapping(source = "position", target = "position", qualifiedByName = "stringToPosition")
	@Mapping(source = "company", target = "company", qualifiedByName = "stringToCompany")
//	@Mapping(target = "company", ignore = true)
	Employee DtoToEmployee(EmployeeDto employee);

	List<Employee> DtosToEmployee(List<EmployeeDto> employees);
}
