package hu.webuni.hr.gyd.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.webuni.hr.gyd.model.Employee;

public abstract class EmployeeServices implements EmployeeService {
	
	Map<Long, Employee> employees = new HashMap<>();
		
	{
			employees.put(1L, new Employee(1L, "Gyetvai Denes", "worker", 200000, LocalDateTime.of(2015, 3, 1, 0, 0)));
			employees.put(2L, new Employee(2L, "Gyetvai Gergely", "worker", 300000, LocalDateTime.of(2013, 7, 11, 0, 0)));
	}
		
	public List<Employee> getAll() {
		return new ArrayList<>(employees.values());
	}
	
	/*public List<Employee> getBySalary(int salary) {
		return employees.values().stream()
				.filter(e -> e.getSalary() > salary)
				.collect(Collectors.toList());
	}*/
	
	public Employee getById(long id) {
		return employees.get(id);
	}
	
	public Employee saveEmployee(Employee employee) {
		employees.put(employee.getEmployeeId(), employee);
		return employee;
	}
	
	public Employee editEmployee(long id, Employee employee) {
		employees.put(id, employee);
		return employee;
	}
	
	public void deleteEmployee(long id) {
		employees.remove(id);
	}
	
}
