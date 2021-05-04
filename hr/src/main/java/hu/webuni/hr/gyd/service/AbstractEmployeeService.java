package hu.webuni.hr.gyd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Position;
import hu.webuni.hr.gyd.repository.EmployeeRepository;

public abstract class AbstractEmployeeService implements EmployeeService {
	
	@Autowired
	EmployeeRepository employeeRepository;
		
	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}
	
	public Optional<Employee> getById(long id) {
		return employeeRepository.findById(id);
	}
	
	@Transactional
	public Employee saveEmployee(Employee employee) {
			return employeeRepository.save(employee);
	}
	
	@Transactional
	public Employee editEmployee(long id, Employee employee) {
		if(employeeRepository.existsById(id))
			return employeeRepository.save(employee);
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
		int salary = example.getSalary();
		LocalDateTime hiringDate = example.getHiringDate();
		
		Specification<Employee> spec = Specification.where(null);
		
		if(id > 0) {
			spec = spec.and(EmployeeSpecifications.hasId(id));
		}
		
		
	}
	
}
