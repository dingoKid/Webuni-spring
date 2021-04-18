package hu.webuni.hr.gyd.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.repository.EmployeeRepository;

public abstract class EmployeeServices implements EmployeeService {
	
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
	
}
