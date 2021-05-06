package hu.webuni.hr.gyd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Position;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.PositionRepository;

public abstract class AbstractEmployeeService implements EmployeeService {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}
	
	public Optional<Employee> getById(long id) {
		return employeeRepository.findById(id);
	}
	
	@Transactional
	public Employee addPositionAndSave(Employee employee) {
		Position position = positionRepository.findByName(employee.getPosition().getName()).orElseThrow(() -> new NoSuchElementException());
		employee.setPosition(position);
		return saveEmployee(employee);
	}
	
	@Transactional
	public Employee addPositionAndEdit(long id, Employee employee) {
		Position position = positionRepository.findByName(employee.getPosition().getName()).orElseThrow(() -> new NoSuchElementException());
		employee.setPosition(position);
		return editEmployee(id, employee);
	}
	
	@Transactional
	public Employee saveEmployee(Employee employee) {
			return employeeRepository.save(employee);
	}
	
	@Transactional
	public Employee editEmployee(long id, Employee employee) {
		if(employeeRepository.existsById(id))
			return saveEmployee(employee);
		else 
			throw new NoSuchElementException();
	}
	
	@Transactional
	public void deleteEmployee(long id) {		
		if(employeeRepository.existsById(id))
			employeeRepository.deleteById(id);
		else
			throw new NoSuchElementException();
	}
	
	public List<Employee> findEmployeesByExample(Employee example) {
		Long id = example.getEmployeeId();
		String name = example.getName();
		Position position = example.getPosition();
		Integer salary = example.getSalary();
		LocalDateTime hiringDate = example.getHiringDate();
		
		System.out.println(hiringDate);
		Company company = example.getCompany();
		
		Specification<Employee> spec = Specification.where(null);
		
		if(id != null) {
			spec = spec.and(EmployeeSpecifications.hasId(id));
		}
		
		if(StringUtils.hasText(name)) {
			spec = spec.and(EmployeeSpecifications.hasName(name));
		}
		
		if(position != null) {
			spec = spec.and(EmployeeSpecifications.hasPosition(position.getName()));
		}
		
		if(salary > 0) {
			spec = spec.and(EmployeeSpecifications.hasSalary(salary));
		}
		
		if(hiringDate != null) {
			hiringDate = hiringDate.withHour(0).withMinute(0);
			spec = spec.and(EmployeeSpecifications.hasHiringDate(hiringDate));
		}
		
		if(company != null) {
			spec = spec.and(EmployeeSpecifications.hasCompany(company.getName()));
		}
		
		return employeeRepository.findAll(spec, Sort.by("name"));
	}
	
}
