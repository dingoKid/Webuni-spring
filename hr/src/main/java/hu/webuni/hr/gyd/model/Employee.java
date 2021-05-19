package hu.webuni.hr.gyd.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long employeeId;
	private String name;
	
	@ManyToOne
	private Position position;
	
	private int salary;
	private LocalDateTime hiringDate;
	
	@ManyToOne//(fetch = FetchType.LAZY)
	private Company company;
	
	@ManyToOne
	private Employee principal;
	
	private String username;
	private String password;
	
	public Employee() {}	
	
	
	public Employee(Long employeeId, String name, Position position, int salary, LocalDateTime hiringDate,
			Company company) {
		super();
		this.employeeId = employeeId;
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.hiringDate = hiringDate;
		this.company = company;
	}

	public Employee(String name, Position position, int salary, LocalDateTime hiringDate, Company company) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.hiringDate = hiringDate;
		this.company = company;
	}

	public Employee getPrincipal() {
		return principal;
	}


	public void setPrincipal(Employee principal) {
		this.principal = principal;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
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

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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
		Employee other = (Employee) obj;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Employee [EmployeeId=" + employeeId + ", name=" + name + ", position=" + position + ", salary=" + salary
				+ ", hiringDate=" + hiringDate + ", company=" + company + "]";
	}

}
