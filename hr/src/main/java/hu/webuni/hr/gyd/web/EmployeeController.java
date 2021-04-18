package hu.webuni.hr.gyd.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.service.EmployeeService;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeMapper mapper;
	
	@Autowired
	EmployeeRepository employeeRepository;

	@GetMapping
	public List<EmployeeDto> getAllEmployees() {
		return mapper.employeesToDtos(employeeService.getAll());
	}

	@GetMapping(params = "salary")
	public List<EmployeeDto> getBySalary(@RequestParam int salary) {
		return mapper.employeesToDtos(
				employeeService.getAll().stream().filter(e -> e.getSalary() > salary).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public EmployeeDto getById(@PathVariable long id) {
		var employee = employeeService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return mapper.employeeToDto(employee);
	}

	@PostMapping
	public EmployeeDto addEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
		Employee employee = mapper.DtoToEmployee(employeeDto);
		employeeService.saveEmployee(employee);
		return mapper.employeeToDto(employee);
	}

	@PutMapping("/{id}")
	public EmployeeDto modifyEmployee(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
		employeeDto.setEmployeeId(id);
		Employee employee = mapper.DtoToEmployee(employeeDto);
		
		try {			
			employeeService.editEmployee(id, employee);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return mapper.employeeToDto(employee);
	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {
		try {
		employeeService.deleteEmployee(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(params = "position")
	public List<EmployeeDto> getByPosition(@RequestParam String position) {
		return mapper.employeesToDtos(employeeRepository.findByPosition(position));
	}
	
	@GetMapping(params = "name")
	public List<EmployeeDto> getByName(@RequestParam String name) {
		return mapper.employeesToDtos(employeeRepository.findByNameStartingWithIgnoreCase(name));
	}
	
	@GetMapping(params = {"start", "end"})
	public List<EmployeeDto> getByDates(@RequestParam LocalDateTime start, LocalDateTime end) {
		return mapper.employeesToDtos(employeeRepository.findByHiringDateBetween(start, end));
	}

}
