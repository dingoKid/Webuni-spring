package hu.webuni.hr.gyd.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.webuni.hr.gyd.service.EmployeeService;
import hu.webuni.hr.gyd.service.EmployeeServices;
import hu.webuni.hr.gyd.service.SmartEmployeeService;

@Configuration
@Profile("smart")
public class SmartEmployeeConfiguration {

	@Bean
	public EmployeeServices employeeServices() {
		return new SmartEmployeeService();
	}
	
	@Bean
	public EmployeeService employeeService() {
		return new SmartEmployeeService();
	}
}
