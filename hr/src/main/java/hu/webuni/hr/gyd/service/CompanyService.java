package hu.webuni.hr.gyd.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;

@Service
public class CompanyService {

	Map<Long, Company> companies = new HashMap<>();

	{
		companies.put(1L, new Company(1L, 121212, "IBM", "7632 Pécs, Lahti utca"));
		companies.put(2L, new Company(2L, 131313, "Firm", "7711 Szeged, Fő utca"));
		companies.put(3L, new Company(3L, 141414, "Epam", "7555 Budapest, Kossuth utca"));
		companies.put(4L, new Company(4L, 151515, "Microsoft", "7988 Szekszárd, Virág utca"));

		companies.get(1L).getEmployees()
				.add(new Employee(1L, "Kiss János", "boss", 525000, LocalDateTime.of(2010, 10, 10, 0, 0)));

		companies.get(1L).getEmployees()
				.add(new Employee(2L, "Nagy Béla", "assistant", 305000, LocalDateTime.of(2015, 03, 20, 0, 0)));
	}
	
	public List<Company> getAll() {
		return new ArrayList<>(companies.values());
	}
	
	public Company getById(long id) {
		return companies.get(id);
	}
	
	public Company saveCompany(Company company) {
		companies.put(company.getCompanyId(), company);
		return company;
	}
	
	public Company editCompany(long id, Company company) {
		companies.put(id, company);
		return company;
	}
	
	public void deleteCompany(long id) {
		companies.remove(id);
	}
	
	public Employee saveEmployee(long companyId, Employee employee) {
		companies.get(companyId).getEmployees().add(employee);
		return employee;
	}
	
	public void deleteEmployee(long companyId, long employeeId) {
		Employee employee = companies.get(companyId).getEmployees().stream().filter(e -> e.getEmployeeId() == employeeId).findFirst().get();
		companies.get(companyId).getEmployees().remove(employee);
	}
	
	public List<Employee> changeEmployeeList(long companyId, List<Employee> employees) {
		companies.get(companyId).setEmployees(employees);
		return employees;
	}

	public Map<Long, Company> getCompanies() {
		return companies;
	}

	public void setCompanies(Map<Long, Company> companies) {
		this.companies = companies;
	}
		
	
}
