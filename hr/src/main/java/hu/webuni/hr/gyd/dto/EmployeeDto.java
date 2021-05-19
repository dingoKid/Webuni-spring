package hu.webuni.hr.gyd.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

public class EmployeeDto {
	
	private Long employeeId;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String position;
	
	@Positive
	private int salary;
	
	@Past
	private LocalDateTime hiringDate;
	
	private String company;
	
	public EmployeeDto() {}
	
	public EmployeeDto(Long employeeId, String name, String position, int salary, LocalDateTime hiringDate) {
		this.employeeId = employeeId;
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.hiringDate = hiringDate;
	}
	

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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
		return "Employee [EmployeeId=" + employeeId + ", name=" + name + ", position=" + position + ", salary=" + salary
				+ ", hiringDate=" + hiringDate + "]";
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeDto other = (EmployeeDto) obj;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		return true;
	}
	
}
