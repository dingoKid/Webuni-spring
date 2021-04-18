package hu.webuni.hr.gyd.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.repository.EmployeeRepository;

@Service
public class CompanyService {
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	public List<Company> getAll() {
		return companyRepository.findAll();
	}
	
	public Company getById(long id) {		
		Company company = companyRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
		return company;
	}
	
	@Transactional
	public Company saveCompany(Company company) {
		return companyRepository.save(company);
	}
	
	@Transactional
	public Company editCompany(long id, Company company) {
		if(companyRepository.existsById(id))
			return companyRepository.save(company);
		else
			throw new NoSuchElementException();
	}
	
	@Transactional
	public void deleteCompany(long id) {
		if(companyRepository.existsById(id))
			companyRepository.deleteById(id);
		else
			throw new NoSuchElementException();
	}
	
	@Transactional
	public Company saveEmployee(long companyId, Employee employee) {
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new NoSuchElementException());
		company.getEmployees().add(employee);
		employeeRepository.save(employee);
		System.out.println(employee);
		System.out.println(company);
		return companyRepository.save(company);
	}
	/*
	@Transactional
	public void deleteEmployee(long companyId, long employeeId) {
		Employee employee = companies.get(companyId).getEmployees().stream().filter(e -> e.getEmployeeId() == employeeId).findFirst().get();
		companies.get(companyId).getEmployees().remove(employee);
	}
	
	@Transactional
	public List<Employee> changeEmployeeList(long companyId, List<Employee> employees) {
		companies.get(companyId).setEmployees(employees);
		return employees;
	}
		*/
	
}
