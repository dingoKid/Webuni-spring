package hu.webuni.hr.gyd.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.gyd.dto.EmployeeDto;

@RestController
@RequestMapping("api/employees")
public class HrController {
	
	Map<Long, EmployeeDto> employees = new HashMap<>();
	
	{
		employees.put(1L, new EmployeeDto(1L, "Gyetvai Denes", "worker", 200000, LocalDateTime.of(2015, 3, 1, 0, 0)));
		employees.put(2L, new EmployeeDto(2L, "Gyetvai Gergely", "worker", 300000, LocalDateTime.of(2013, 7, 11, 0, 0)));
	}
	
	@GetMapping
	public List<EmployeeDto> getAllEmployees(){
		return new ArrayList<>(employees.values());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getById(@PathVariable long id) {
		if(!employees.containsKey(id)) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(employees.get(id));
	}
	
	@PostMapping
	public EmployeeDto addEmployee(@RequestBody EmployeeDto employee) {
		employees.put(employee.getEmployeeId(), employee);
		return employee;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> modifyEmployee(@PathVariable long id, @RequestBody EmployeeDto employee) {
		if(!employees.containsKey(id)) return ResponseEntity.notFound().build();
		employee.setEmployeeId(id);
		employees.put(id, employee);
		return ResponseEntity.ok(employee);
	}
	
	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {
		if(employees.containsKey(id))
			employees.remove(id);
	}
	
	
	

}
