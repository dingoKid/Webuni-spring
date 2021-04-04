package hu.webuni.hr.gyd.dto;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {

	private long companyId;	
	private long tradeRegisterNumber;
	private String name;
	private String address;
	private List<EmployeeDto> employees;
			
	public CompanyDto(long companyId, long tradeRegisterNumber, String name, String address) {
		this.companyId = companyId;
		this.tradeRegisterNumber = tradeRegisterNumber;
		this.name = name;
		this.address = address;
		employees = new ArrayList<>();
	}
	
	// copy constructor
	public CompanyDto(CompanyDto company) {
		this.companyId = company.companyId;
		this.tradeRegisterNumber = company.tradeRegisterNumber;
		this.name = company.name;
		this.address = company.address;
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
	public List<EmployeeDto> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}

	@Override
	public String toString() {
		return "CompanyDto [companyId=" + companyId + ", tradeRegisterNumber=" + tradeRegisterNumber + ", name=" + name
				+ ", address=" + address + ", employees=" + employees + "]";
	}

	
	
}
