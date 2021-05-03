package hu.webuni.hr.gyd.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.CompanyType;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Position;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.repository.CompanyTypeRepository;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.PositionRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class CompanyServiceIT {

	@Autowired
	CompanyService companyService;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	CompanyTypeRepository companyTypeRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@BeforeEach
	public void init() {
		employeeRepository.deleteAll();
		companyRepository.deleteAll();
		//positionRepository.deleteAll();
		//companyTypeRepository.deleteAll();
	}
	
	@Test
	void testAddEmployee() throws Exception {		
		CompanyType companyType = companyTypeRepository.findByName("KFT");
		Company companyBefore = new Company(12345, "IBM", "Pecs", companyType);
		companyBefore = companyService.saveCompany(companyBefore);
		Position position = positionRepository.findByName("Driver").get();
		Employee employeeToBeAdded = new Employee("employee1", position, 250000, LocalDateTime.of(2015,  10, 10, 0, 0), null);
		
		Company companyAfter = companyService.addEmployee(companyBefore.getCompanyId(), employeeToBeAdded);
		Employee employeeInCompanyList = companyService.getByIdWithEmployees(companyAfter.getCompanyId()).getEmployees().get(0);
		
		assertThat(employeeToBeAdded).usingRecursiveComparison()
			.ignoringFields("employeeId", "company")
			.isEqualTo(employeeInCompanyList);
		
		assertThat(employeeInCompanyList.getCompany().getCompanyId()).isEqualTo(companyAfter.getCompanyId());		
	}
	
	@Test
	void testDeleteEmployee() throws Exception {		
		CompanyType companyType = companyTypeRepository.findByName("KFT");
		Company companyBefore = new Company(12345, "IBM", "Pecs", companyType);
		companyBefore = companyService.saveCompany(companyBefore);
		Position position = positionRepository.findByName("Driver").get();
		Employee employeeToBeAdded = new Employee("employee1", position, 250000, LocalDateTime.of(2015,  10, 10, 0, 0), null);		
		Company companyAfter = companyService.addEmployee(companyBefore.getCompanyId(), employeeToBeAdded);		
		assertThat(companyService.getByIdWithEmployees(companyAfter.getCompanyId()).getEmployees().size()).isEqualTo(1);
		
		Employee employeeInCompanyList = companyService.getByIdWithEmployees(companyAfter.getCompanyId()).getEmployees().get(0);
		companyService.deleteEmployee(companyAfter.getCompanyId(), employeeInCompanyList.getEmployeeId());
		
		assertThat(companyService.getByIdWithEmployees(companyAfter.getCompanyId()).getEmployees()).doesNotContain(employeeInCompanyList);
		assertThat(companyService.getByIdWithEmployees(companyAfter.getCompanyId()).getEmployees().size()).isEqualTo(0);
		
		Long idInDatabase = employeeInCompanyList.getEmployeeId();
		Employee deletedEmployee = employeeRepository.findById(idInDatabase).get();
		assertThat(deletedEmployee.getCompany()).isNull();			
	}
}
