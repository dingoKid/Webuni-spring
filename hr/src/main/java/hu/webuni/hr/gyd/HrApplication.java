package hu.webuni.hr.gyd;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;
	
	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Employee e1 = new Employee(1L, "kd", "worker", 100000, LocalDateTime.of(2017, 2, 2, 0, 0));
		
		salaryService.setNewSalary(e1);
		System.out.println(e1.getSalary());
		
		
		
		
	}

}
