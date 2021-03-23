package hu.webuni.hr.gyd.service;

import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Employee;

@Service
public class SalaryService {
	
	private EmployeeService employeeService;
	
	public SalaryService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public void setNewSalary(Employee employee) {
		
		int newSalary = (int) (employee.getSalary() * (100 + employeeService.getPayRaisePercent(employee)) / 100d);
		employee.setSalary(newSalary);
		
	}

}
