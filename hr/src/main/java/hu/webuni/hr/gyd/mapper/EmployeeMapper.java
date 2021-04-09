package hu.webuni.hr.gyd.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.gyd.dto.EmployeeDto;
import hu.webuni.hr.gyd.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	List<EmployeeDto> employeesToDtos(List<Employee> emloyees);
	
	EmployeeDto employeeToDto(Employee employee);

	Employee DtoToEmployee(EmployeeDto employee);

	List<Employee> DtosToEmployee(List<EmployeeDto> employees);
}
