package hu.webuni.hr.gyd.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

public class EmployeeDto {
	
	private Long EmployeeId;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String position;
	
	@Positive
	private int salary;
	
	@Past
	private LocalDateTime hiringDate;
	
	public EmployeeDto(Long employeeId, String name, String position, int salary, LocalDateTime hiringDate) {
		EmployeeId = employeeId;
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.hiringDate = hiringDate;
	}

	public Long getEmployeeId() {
		return EmployeeId;
	}

	public void setEmployeeId(Long employeeId) {
		EmployeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public LocalDateTime getHiringDate() {
		return hiringDate;
	}

	public void setHiringDate(LocalDateTime hiringDate) {
		this.hiringDate = hiringDate;
	}

	@Override
	public String toString() {
		return "Employee [EmployeeId=" + EmployeeId + ", name=" + name + ", position=" + position + ", salary=" + salary
				+ ", hiringDate=" + hiringDate + "]";
	}	
	
	
}
