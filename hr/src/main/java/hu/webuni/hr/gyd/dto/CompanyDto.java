package hu.webuni.hr.gyd.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class CompanyDto {

	private long companyId;
	
	@Positive
	private long tradeRegisterNumber;
	
	@NotEmpty
	private String name;
		
	@NotEmpty
	private String address;
	private String companyType;
	
	private List<EmployeeDto> employees;
	
	public CompanyDto() {}
			
	public CompanyDto(long companyId, long tradeRegisterNumber, String name, String companyType, String address) {
		this.companyId = companyId;
		this.tradeRegisterNumber = tradeRegisterNumber;
		this.name = name;
		this.address = address;
		this.companyType = companyType;
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
	public List<EmployeeDto> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}
	
	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	@Override
	public String toString() {
		return "CompanyDto [companyId=" + companyId + ", tradeRegisterNumber=" + tradeRegisterNumber + ", name=" + name
				+ ", address=" + address + ", employees=" + employees + "]";
	}

	
	
}
