package hu.webuni.hr.gyd;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.gyd.configuration.HrConfigPropertiesWithLists;
import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Position;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.service.InitDbService;
import hu.webuni.hr.gyd.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;
	
	@Autowired
	HrConfigPropertiesWithLists config;
	
	@Autowired
	InitDbService initDbService;
	
	@Autowired
	CompanyRepository companyRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Employee e1 = new Employee(1L, "kd", new Position(), 100000, LocalDateTime.of(2003, 2, 2, 0, 0), new Company());
		salaryService.setNewSalary(e1);
		System.out.println(e1.getSalary());
		initDbService.clearDB();
		initDbService.insertTestData();
		
		/*var list = companyRepository.findBySalary(450000);
		list.forEach(c -> System.out.println(c.getName()));*/
	}

}
