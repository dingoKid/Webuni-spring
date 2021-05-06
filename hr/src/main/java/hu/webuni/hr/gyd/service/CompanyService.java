package hu.webuni.hr.gyd.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Position;
import hu.webuni.hr.gyd.model.PositionSalary;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.repository.CompanyTypeRepository;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.PositionRepository;

@Service
public class CompanyService {
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CompanyTypeRepository companyTypeRepository;

	@Autowired
	PositionRepository positionTypeRepository;
	
	public List<Company> getAll() {
		return companyRepository.findAll(Sort.by("name"));
	}
	
	public List<Company> getAllWithEmployees() {
		return companyRepository.findAllWithEmployees();
	}
	
	public Company getByIdWithEmployees(long id) {
		Company company = companyRepository.findByIdWithEmployees(id);
		if(company != null)
			return company;
		else
			throw new NoSuchElementException();
	}
	
	public Company getById(long id) {
		Company company = companyRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
		return company;
	}
	
	@Transactional
	public Company saveCompany(Company company) {
		var companyType = companyTypeRepository.findByName(company.getCompanyType().getName());
		if(companyType == null)
			throw new NoSuchElementException();
		company.setCompanyType(companyType);
		return companyRepository.save(company);
	}
	
	@Transactional
	public Company editCompany(long id, Company company) {
		if(companyRepository.existsById(id))
			return saveCompany(company);
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
	public Company addEmployee(long companyId, Employee employee) {
		Position position = positionTypeRepository.findByName(employee.getPosition().getName()).orElseThrow(() -> new NoSuchElementException());
		employee.setPosition(position);
		Company company = companyRepository.findByIdWithEmployees(companyId);
		if(company == null)
			throw new NoSuchElementException();
		company.addEmployee(employee);
		employeeRepository.save(employee);
		return company;
	}
	
	@Transactional
	public void deleteEmployee(long companyId, long employeeId) {
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchElementException());
		Company company = companyRepository.findByIdWithEmployees(companyId);
		if(company == null)
			throw new NoSuchElementException();
		company.deleteEmployee(employee);
		//employeeRepository.save(employee);
	}
	
	@Transactional
	public Company changeEmployeeList(long companyId, List<Employee> employees) {
		Company company = companyRepository.findByIdWithEmployees(companyId);
		if(company == null)
			throw new NoSuchElementException();
		
		company.clearEmployeeList();
		
		for(Employee employee : employees) {
			company.addEmployee(employee);
			employeeRepository.save(employee);
		}
		return company;
	}
	
	public List<PositionSalary> getCompanyAverages(long companyId) {
		if(companyRepository.existsById(companyId))
			return companyRepository.findSalariesById(companyId);
		else
			throw new NoSuchElementException();
	}
		
		
	
	
}
