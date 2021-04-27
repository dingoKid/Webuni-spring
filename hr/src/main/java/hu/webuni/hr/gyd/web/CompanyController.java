package hu.webuni.hr.gyd.web;

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

import hu.webuni.hr.gyd.dto.CompanyDto;
import hu.webuni.hr.gyd.dto.EmployeeDto;
import hu.webuni.hr.gyd.mapper.CompanyMapper;
import hu.webuni.hr.gyd.mapper.EmployeeMapper;
import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
	
	@Autowired 
	CompanyService companyService;
		
	@Autowired
	CompanyMapper companyMapper;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@GetMapping
	public List<CompanyDto> allCompanies(@RequestParam(required = false, defaultValue = "false") boolean full) {
		List<Company> companies = companyService.getAll();
		return full ? companyMapper.companiesToDtos(companies) : companyMapper.companiesWOEmployeesToDtos(companies);
	}
	
	@GetMapping("/{id}")
	public CompanyDto getById(@RequestParam(required = false, defaultValue = "false") boolean full, @PathVariable long id) {
		try {
			Company company = companyService.getById(id);
			return full ? companyMapper.companyToDto(company) : companyMapper.companyWOEmployeesToDto(company);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}		
	}
	
	@PostMapping
	public CompanyDto addCompany(@RequestBody @Valid CompanyDto companyDto) {
		Company company = companyMapper.DtoToCompany(companyDto);
		try {
			return companyMapper.companyToDto(companyService.saveCompany(company));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/{id}")
	public CompanyDto modifyCompany(@PathVariable long id, @RequestBody @Valid CompanyDto companyDto) {
		companyDto.setCompanyId(id);
		Company company = companyMapper.DtoToCompany(companyDto);
		try {
			companyService.editCompany(id, company);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return companyMapper.companyToDto(company);
	}		
	
	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		try {
			companyService.deleteCompany(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}	
		
	@PostMapping("/{companyId}/hire")
	public CompanyDto hireEmployee(@PathVariable long companyId, @RequestBody @Valid EmployeeDto employeeDto) {
		Employee employee = employeeMapper.DtoToEmployee(employeeDto);
		try {
			return companyMapper.companyToDto(companyService.addEmployee(companyId, employee));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}		
	}
	
	@DeleteMapping("{companyId}/delete/{employeeId}")
	public void deleteEmployee(@PathVariable long companyId, @PathVariable long employeeId) {
		try {
			companyService.deleteEmployee(companyId, employeeId);			
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/{companyId}/swapteam")
	public CompanyDto swapEmployees(@PathVariable long companyId, @RequestBody List<EmployeeDto> employees) {
		try {
			return companyMapper.companyToDto(companyService.changeEmployeeList(companyId, employeeMapper.DtosToEmployee(employees)));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/salaryover")
	public List<CompanyDto> getBySalary(@RequestParam int salary) {
		return companyMapper.companiesToDtos(companyRepository.findBySalary(salary));
	}
	
	@GetMapping("/employeesover")
	public List<CompanyDto> getByEmployeeNumber(@RequestParam int number) {
		List<Company> companies = companyRepository.findAll().stream()
			.filter(c -> c.getEmployees().size() > number)
			.collect(Collectors.toList());
		return companyMapper.companiesToDtos(companies);
	}
	
	
	
	

}
