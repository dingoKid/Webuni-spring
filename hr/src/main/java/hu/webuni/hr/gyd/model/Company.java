package hu.webuni.hr.gyd.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
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
	
	@OneToMany(mappedBy = "company")
	private List<Employee> employees;
	
	@OneToOne(mappedBy = "company")
	private Position position;
	
	public Company() { }
			
	public Company(Long companyId, long tradeRegisterNumber, String name, String address, CompanyType companyType) {
		this.companyId = companyId;
		this.tradeRegisterNumber = tradeRegisterNumber;
		this.name = name;
		this.address = address;
		this.companyType = companyType;
		employees = new ArrayList<>();
	}	
	
	public Company(long tradeRegisterNumber, String name, String address, CompanyType companyType) {
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

	public Long getCompanyId() {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((companyType == null) ? 0 : companyType.hashCode());
		result = prime * result + ((employees == null) ? 0 : employees.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + (int) (tradeRegisterNumber ^ (tradeRegisterNumber >>> 32));
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
		Company other = (Company) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (companyType == null) {
			if (other.companyType != null)
				return false;
		} else if (!companyType.equals(other.companyType))
			return false;
		if (employees == null) {
			if (other.employees != null)
				return false;
		} else if (!employees.equals(other.employees))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (tradeRegisterNumber != other.tradeRegisterNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompanyDto [companyId=" + companyId + ", tradeRegisterNumber=" + tradeRegisterNumber + ", name=" + name
				+ ", address=" + address + ", employees=" + employees + "]";
	}


	
}
