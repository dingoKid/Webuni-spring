package hu.webuni.hr.gyd.service;

import java.util.List;
import java.util.Optional;

import hu.webuni.hr.gyd.model.Employee;

public interface EmployeeService {

	int getPayRaisePercent(Employee employee);
	
	public List<Employee> getAll();
	
	public Optional<Employee> getById(long id);
	
	public Employee saveEmployee(Employee employee);
	
	public Employee editEmployee(long id, Employee employee);
	
	public void deleteEmployee(long id);
	
	public List<Employee> findEmployeesByExample(Employee example);
	
}
