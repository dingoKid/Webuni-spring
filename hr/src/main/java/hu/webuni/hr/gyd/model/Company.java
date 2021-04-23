package hu.webuni.hr.gyd.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Company {

	@Id
	@GeneratedValue
	private long companyId;
	
	private long tradeRegisterNumber;
	private String name;
	private String address;
	
	@OneToMany(mappedBy = "company")
	private List<Employee> employees;
	
	public Company() { }
			
	public Company(long companyId, long tradeRegisterNumber, String name, String address) {
		this.companyId = companyId;
		this.tradeRegisterNumber = tradeRegisterNumber;
		this.name = name;
		this.address = address;
		employees = new ArrayList<>();
	}
	
	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	
	public long getTradeRegisterNumber() {
		return tradeRegisterNumber;
	}
	public void setTradeRegisterNumber(long tradeRegisterNumber) {
		this.tradeRegisterNumber = tradeRegisterNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public void addEmployee(Employee employee) {
		if(employees == null) 
			this.employees = new ArrayList<Employee>();
		this.employees.add(employee);
		employee.setCompany(this);			
	}
	
	public void deleteEmployee(Employee employee) {
		this.employees.remove(employee);
		employee.setCompany(null);
		
	}
	
	public void clearEmployeeList() {
		for(Employee employee : this.employees) {
			employee.setCompany(null);
		}
		this.getEmployees().clear();
	}
	
	@Override
	public String toString() {
		return "CompanyDto [companyId=" + companyId + ", tradeRegisterNumber=" + tradeRegisterNumber + ", name=" + name
				+ ", address=" + address + ", employees=" + employees + "]";
	}


	
}
