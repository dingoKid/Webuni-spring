package hu.webuni.hr.gyd.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hu.webuni.hr.gyd.model.Employee;

@Controller
public class HrTLController {
	
	List<Employee> employees = new ArrayList<Employee>();
		
	{
			employees.add(new Employee(1L, "Gyetvai Denes", "worker", 200000, LocalDateTime.of(2015, 3, 1, 0, 0)));
			employees.add(new Employee(2L, "Gyetvai Gergely", "worker", 300000, LocalDateTime.of(2013, 7, 11, 0, 0)));
	}
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/employees")
	public String getEmployees(Map<String, Object> model) {
		model.put("employees", employees);
		model.put("newEmployee", new Employee());
		return "employees";
	}
	
	@PostMapping("/employees")
	public String addEmployee(Employee employee) {
		if(!employees.stream().map(e -> e.getEmployeeId()).collect(Collectors.toList()).contains(employee.getEmployeeId()))
			employees.add(employee);
		return "redirect:employees";
	}
	

}
