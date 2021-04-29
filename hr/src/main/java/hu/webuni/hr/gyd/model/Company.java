package hu.webuni.hr.gyd.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long companyId;
	
	private long tradeRegisterNumber;
	private String name;
	private String address;
	
	@ManyToOne //(fetch = FetchType.LAZY)
	private CompanyType companyType;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
	private List<Employee> employees;
	
	@OneToOne(mappedBy = "company")
	private Position position;
	
	public Company() { }
			
	public Company(long companyId, long tradeRegisterNumber, String name, String address, CompanyType companyType) {
		this.companyId = companyId;
		this.tradeRegisterNumber = tradeRegisterNumber;
		this.name = name;
		this.address = address;
		this.companyType = companyType;
		employees = new ArrayList<>();
	}	
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public long getCompanyId() {
		return companyId;
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
	
	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
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
