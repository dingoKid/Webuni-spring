package hu.webuni.hr.gyd.web;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;

@Controller
public class HrTLController {
	
	Map<Long, Employee> employees = new HashMap<>();
		
	{
			employees.put(1L, new Employee(1L, "Gyetvai Denes", "worker", 300000, LocalDateTime.of(2015, 3, 1, 0, 0), new Company()));
			employees.put(2L, new Employee(2L, "Kovács János", "manager", 400000, LocalDateTime.of(2013, 7, 11, 0, 0), new Company()));
			employees.put(3L, new Employee(3L, "Kiss Béla", "assistant", 200000, LocalDateTime.of(2008, 1, 11, 0, 0), new Company()));
			employees.put(4L, new Employee(4L, "Nagy Péter", "other", 150000, LocalDateTime.of(2020, 11, 11, 0, 0), new Company()));
	}
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/employees")
	public String getEmployees(Map<String, Object> model) {
		model.put("employees", employees.values());
		model.put("newEmployee", new Employee());
		return "employees";
	}
	
	@GetMapping("/employees/edit/{id}")
	public String getEmployee(@PathVariable long id, Map<String, Object> model) {
		var employee = employees.get(id);
		if(employee == null) return "redirect:/employees";
		model.put("employee", employee);
		return "employee";
	}
	
	@PostMapping("/employees/edit")
	public String editEmployee(Employee employee) {
		employees.put(employee.getEmployeeId(), employee);
		return "redirect:/employees";
	}
	
	@PostMapping("/employees")
	public String addEmployee(Employee employee) {
		if(!employees.keySet().contains(employee.getEmployeeId()))
			employees.put(employee.getEmployeeId(), employee);
		return "redirect:employees";
	}
	
	@GetMapping("/employees/delete/{id}")
	public String deleteEmployee(@PathVariable("id") long id) {
		if(employees.keySet().contains(id)) 
			employees.remove(id);
		return "redirect:/employees";
	}
	

}
