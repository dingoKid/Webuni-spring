package hu.webuni.hr.gyd.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import hu.webuni.hr.gyd.security.Employee;

public class HrUserDetails extends User {
	
	private String employeeName;	
	private Long employeeId;
	
	private List<Employee> employees;
	private Employee principal;

	public HrUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		// TODO Auto-generated constructor stub
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public Employee getPrincipal() {
		return principal;
	}

	public void setPrincipal(Employee principal) {
		this.principal = principal;
	}
	
	
	
	/*private Employee employee;

	public HrUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Employee employee) {
		super(username, password, authorities);
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}*/
	
	

}
