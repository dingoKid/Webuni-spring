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

import hu.webuni.hr.gyd.dto.CompanyDto;
import hu.webuni.hr.gyd.dto.EmployeeDto;
import hu.webuni.hr.gyd.mapper.CompanyMapper;
import hu.webuni.hr.gyd.mapper.EmployeeMapper;
import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.service.CompanyService;
import hu.webuni.hr.gyd.service.NonUniqueIdException;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
	
	@Autowired 
	CompanyService companyService;
	
	@Autowired
	CompanyMapper companyMapper;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@GetMapping
	public List<CompanyDto> allCompanies(@RequestParam(required = false, defaultValue = "false") boolean full) {
		if(!full) {
			return companyMapper.companiesToDtos(companyService.getAll()).stream()
					.map(c -> new CompanyDto(c))
					.collect(Collectors.toList());
		}
		return companyMapper.companiesToDtos(companyService.getAll());
	}
	
	@GetMapping("/{id}")
	public CompanyDto getById(@RequestParam(required = false, defaultValue = "false") boolean full, @PathVariable long id) {
		Company company = companyService.getById(id);
		if(company == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		if(full) return companyMapper.companyToDto(company);
		return new CompanyDto(companyMapper.companyToDto(company));
	}
	
	@PostMapping
	public CompanyDto addCompany(@RequestBody @Valid CompanyDto companyDto) {
		if(companyService.getById(companyDto.getCompanyId()) != null) 
			throw new NonUniqueIdException();
		companyService.saveCompany(companyMapper.DtoToCompany(companyDto));
		return companyDto;
	}
	
	@PutMapping("/{id}")
	public CompanyDto modifyCompany(@PathVariable long companyId, @RequestBody @Valid CompanyDto companyDto) {
		checkCompanyExist(companyId);
		companyDto.setCompanyId(companyId);
		companyService.editCompany(companyId, companyMapper.DtoToCompany(companyDto));
		return companyDto;
	}
	
	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long companyId) {
		checkCompanyExist(companyId);
		companyService.deleteCompany(companyId);
	}
	
	@PostMapping("/{companyId}/hire")
	public EmployeeDto hireEmployee(@PathVariable long companyId, @RequestBody @Valid EmployeeDto employeeDto) {
		checkCompanyExist(companyId);
		if(companyService.getById(companyId).getEmployees().stream()
				.anyMatch(e -> e.getEmployeeId() == employeeDto.getEmployeeId()))
					throw new NonUniqueIdException();
		companyService.saveEmployee(companyId, employeeMapper.DtoToEmployee(employeeDto));
		return employeeDto;
	}
	
	@DeleteMapping("{companyId}/delete/{employeeId}")
	public void deleteEmployee(@PathVariable long companyId, @PathVariable long employeeId) {
		checkCompanyExist(companyId);
		if(companyService.getCompanies().get(companyId).getEmployees().stream()
				.anyMatch(e -> e.getEmployeeId() == employeeId))
					companyService.deleteEmployee(companyId, employeeId);
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{companyId}/swapteam")
	public List<EmployeeDto> swapEmployees(@PathVariable long companyId, @RequestBody List<EmployeeDto> employees) {
		checkCompanyExist(companyId);
		companyService.changeEmployeeList(companyId, employeeMapper.DtosToEmployee(employees));
		return employees;
	}
	
	private void checkCompanyExist(long companyId) {
		if(companyService.getById(companyId) == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	}

}
