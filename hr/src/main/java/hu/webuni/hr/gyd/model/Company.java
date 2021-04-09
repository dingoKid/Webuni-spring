package hu.webuni.hr.gyd.model;

import java.util.ArrayList;
import java.util.List;

public class Company {

	private long companyId;	
	private long tradeRegisterNumber;
	private String name;
	private String address;
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

	@Override
	public String toString() {
		return "CompanyDto [companyId=" + companyId + ", tradeRegisterNumber=" + tradeRegisterNumber + ", name=" + name
				+ ", address=" + address + ", employees=" + employees + "]";
	}

	
	
}
