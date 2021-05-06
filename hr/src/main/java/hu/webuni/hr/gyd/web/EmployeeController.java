package hu.webuni.hr.gyd.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Position;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.PositionRepository;
import hu.webuni.hr.gyd.service.EmployeeService;
import hu.webuni.hr.gyd.service.SalaryService;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeMapper mapper;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	SalaryService salaryService;

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
		try {
			employee = employeeService.addPositionAndSave(employee);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return mapper.employeeToDto(employee);
	}

	@PutMapping("/{id}")
	public EmployeeDto modifyEmployee(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
		employeeDto.setEmployeeId(id);
		Employee employee = mapper.DtoToEmployee(employeeDto);
		
		try {			
			employee = employeeService.addPositionAndEdit(id, employee);
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
	public List<EmployeeDto> getByPosition(@RequestParam String position, Pageable page) {	
		Position pos = positionRepository.findByName(position).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Page<Employee> pageResult = employeeRepository.findByPosition(pos, page);
		System.out.println();
		System.out.println(pageResult.getNumber());
		System.out.println(pageResult.getNumberOfElements());
		System.out.println(pageResult.getSize());
		System.out.println(pageResult.getTotalElements());
		System.out.println(pageResult.getTotalPages());
		System.out.println(pageResult.getPageable());
		return mapper.employeesToDtos(pageResult.toList());
	}
	
	@GetMapping(params = "name")
	public List<EmployeeDto> getByName(@RequestParam String name) {
		return mapper.employeesToDtos(employeeRepository.findByNameStartingWithIgnoreCase(name));
	}
	
	@GetMapping(params = {"start", "end"})
	public List<EmployeeDto> getByDates(@RequestParam LocalDateTime start, LocalDateTime end) {
		return mapper.employeesToDtos(employeeRepository.findByHiringDateBetween(start, end));
	}		
	
	@GetMapping(path = "/raisesalary/{companyId}/{position}", params = "newsalary")
	public List<EmployeeDto> raisePositionSalary(@PathVariable long companyId, @PathVariable String position, @RequestParam int newsalary) {
		try {
			return mapper.employeesToDtos(salaryService.raisePositionSalary(position, newsalary, companyId));
		} catch(NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(path = "/search")
	public List<EmployeeDto> searchEmployees(@RequestParam(required = false) Long employeeid,
											@RequestParam(required = false) String name,
											@RequestParam(required = false) String position,
											@RequestParam(required = false) Integer salary,
											@RequestParam(required = false) LocalDateTime hiringdate,
											@RequestParam(required = false) String companyname) {
		Employee example = new Employee();
		
		if(position != null) {
			Position pos = positionRepository.findByName(position).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			example.setPosition(pos);
		}
		
		if(companyname != null) {
			Company company = companyRepository.findByName(companyname).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			example.setCompany(company);
		}
		
		if(salary == null)
			example.setSalary(0);
		else
			example.setSalary(salary);
		
		example.setEmployeeId(employeeid);
		example.setName(name);		
		example.setHiringDate(hiringdate);
		return mapper.employeesToDtos(employeeService.findEmployeesByExample(example));
	}
	
	
	
	
	
	
	
	

}
