package hu.webuni.hr.gyd.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.gyd.dto.EmployeeDto;
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
		
	@Mapping(source = "position.name", target = "position")
	EmployeeDto employeeToDto(Employee employee);

	@Mapping(source = "position", target = "position", qualifiedByName = "stringToPosition")
	@Mapping(target = "company", ignore = true)
	Employee DtoToEmployee(EmployeeDto employee);

	List<Employee> DtosToEmployee(List<EmployeeDto> employees);
}
