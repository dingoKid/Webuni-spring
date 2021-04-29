package hu.webuni.hr.gyd.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Position;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.PositionRepository;

@Service
public class SalaryService {
	
	private EmployeeService employeeService;
	private PositionRepository positionRepository;
	private EmployeeRepository employeeRepository;
	private CompanyRepository companyRepository;
	
	public SalaryService(EmployeeService employeeService, PositionRepository positionRepository,
			EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
		super();
		this.employeeService = employeeService;
		this.positionRepository = positionRepository;
		this.employeeRepository = employeeRepository;
		this.companyRepository = companyRepository;
	}

	public void setNewSalary(Employee employee) {
		
		int newSalary = (int) (employee.getSalary() * (100 + employeeService.getPayRaisePercent(employee)) / 100d);
		employee.setSalary(newSalary);
		
	}
	
//	@Transactional
//	public List<Employee> raisePositionSalary(String position, int newsalary) {
//		Position pos = positionRepository.findByName(position).orElseThrow(() -> new NoSuchElementException());
//		pos.setMinSalary(newsalary);
//		List<Employee> employees = employeeRepository.findByPosition(pos, null).toList();
//		List<Employee> modifiedEmployees = new ArrayList<>();
//		for (Employee employee : employees) {
//			if(employee.getSalary() < newsalary) {
//				employee.setSalary(newsalary);
//				modifiedEmployees.add(employee);
//			}
//		}
//		return modifiedEmployees;
//	}
	
	@Transactional
	public List<Employee> raisePositionSalary(String position, int newsalary, long companyId) {
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new NoSuchElementException());
		Position pos = positionRepository.findByName(position).orElseThrow(() -> new NoSuchElementException());
		pos.setMinSalary(newsalary);
		List<Employee> employees = employeeRepository.findByCompanyAndPosition(company, pos);
		for (Employee employee : employees) {
			if(employee.getSalary() < newsalary) {
				employee.setSalary(newsalary);
			}
		}
		return employees;
	}

}
