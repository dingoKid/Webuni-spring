package hu.webuni.hr.gyd.web;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.gyd.dto.CompanyDto;
import hu.webuni.hr.gyd.dto.EmployeeDto;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
	
	 Map<Long, CompanyDto> companies = new HashMap<>();
	
	{
		companies.put(1L, new CompanyDto(1L, 121212, "IBM", "7632 Pécs, Lahti utca"));
		companies.put(2L, new CompanyDto(2L, 131313, "Firm", "7711 Szeged, Fő utca"));
		companies.put(3L, new CompanyDto(3L, 141414, "Epam", "7555 Budapest, Kossuth utca"));
		companies.put(4L, new CompanyDto(4L, 151515, "Microsoft", "7988 Szekszárd, Virág utca"));
		
		companies.get(1L).getEmployees()
			.add(new EmployeeDto(1L, "Kiss János", "boss", 525000, LocalDateTime.of(2010, 10, 10, 0, 0)));
	}
	
	@GetMapping
	public List<CompanyDto> allCompanies(@RequestParam(required = false, defaultValue = "false") boolean full) {
		if(!full) {
			return companies.values().stream()
					.map(c -> new CompanyDto(c))
					.collect(Collectors.toList());
		}
		return companies.values().stream()
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> getById(@RequestParam(required = false, defaultValue = "false") boolean full, @PathVariable long id) {
		if(!companies.containsKey(id)) {
			ResponseEntity.notFound().build();
		}
		var company = companies.get(id);
		if(full) return ResponseEntity.ok(company);
		return ResponseEntity.ok(new CompanyDto(company));
	}
	
	@PostMapping
	public ResponseEntity<CompanyDto> addCompany(@RequestBody CompanyDto company) {
		if(companies.containsKey(company.getCompanyId())) return ResponseEntity.badRequest().build();
		companies.put(company.getCompanyId(), company);
		return ResponseEntity.ok(company);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long id, @RequestBody CompanyDto company) {
		if(!companies.keySet().contains(id)) return ResponseEntity.notFound().build();
		company.setCompanyId(id);
		companies.put(id, company);
		return ResponseEntity.ok(company);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteCompany(@PathVariable long id) {
		if(companies.containsKey(id)) return ResponseEntity.notFound().build();
		companies.remove(id);
		return ResponseEntity.ok(true);
	}
	
	@PostMapping("/{companyId}/hire")
	public ResponseEntity<EmployeeDto> hireEmployee(@PathVariable long companyId, @RequestBody EmployeeDto employee) {
		if(!companies.containsKey(companyId)) return ResponseEntity.notFound().build();
		companies.get(companyId).getEmployees().add(employee);
		return ResponseEntity.ok(employee);
	}
	
	@DeleteMapping("{companyId}/delete/{employeeId}")
	public ResponseEntity<Boolean> deleteEmployee(@PathVariable long companyId, @PathVariable long employeeId) {
		if(!companies.containsKey(companyId)
				|| companies.get(companyId).getEmployees().stream()
				.noneMatch(e -> e.getEmployeeId() == employeeId)) 
					return ResponseEntity.notFound().build();
		
		var employee = companies.get(companyId).getEmployees().stream()
				.filter(e -> e.getEmployeeId() == employeeId).findFirst().get();
		companies.get(companyId).getEmployees().remove(employee);
		
		return ResponseEntity.ok(true);
	}
	
	@PutMapping("/{companyId}/swapteam")
	public ResponseEntity<List<EmployeeDto>> swapEmployees(@PathVariable long companyId, @RequestBody List<EmployeeDto> employees) {
		if(!companies.containsKey(companyId)) return ResponseEntity.notFound().build();
		companies.get(companyId).setEmployees(employees);
		return ResponseEntity.ok(employees);
	}

}
