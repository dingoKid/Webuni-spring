package hu.webuni.hr.gyd.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.gyd.dto.EmployeeDto;
import hu.webuni.hr.gyd.mapper.EmployeeMapper;
import hu.webuni.hr.gyd.service.EmployeeService;
import hu.webuni.hr.gyd.service.NonUniqueIdException;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	EmployeeMapper mapper;
	
	@GetMapping
	public List<EmployeeDto> getAllEmployees(){
		return mapper.employeesToDtos(employeeService.getAll());
	}
	
	@GetMapping(params = "salary")
	public List<EmployeeDto> getBySalary(@RequestParam int salary) {
		return mapper.employeesToDtos(employeeService.getAll().stream()
										.filter(e -> e.getSalary() > salary)
										.collect(Collectors.toList()));
	}
	
	@GetMapping("/{id}")
	public EmployeeDto getById(@PathVariable long id) {
		var employee = employeeService.getById(id);
		if(employee == null) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return mapper.employeeToDto(employee);
	}
	
	@PostMapping
	public EmployeeDto addEmployee(@RequestBody @Valid EmployeeDto employee) {
		if(employeeService.getById(employee.getEmployeeId()) != null)
			throw new NonUniqueIdException();
		employeeService.saveEmployee(mapper.DtoToEmployee(employee));
		return employee;
	}
	
	@PutMapping("/{id}")
	public EmployeeDto modifyEmployee(@PathVariable long id, @RequestBody @Valid EmployeeDto employee) {
		if(employeeService.getById(id) == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		employee.setEmployeeId(id);
		employeeService.editEmployee(id, mapper.DtoToEmployee(employee));
		return employee;
	}
	
	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {
		if(employeeService.getById(id) == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		employeeService.deleteEmployee(id);
	}
	
}
