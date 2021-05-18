package hu.webuni.hr.gyd.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.webuni.hr.gyd.service.DefaultEmployeeService;
import hu.webuni.hr.gyd.service.EmployeeService;

@Configuration
@Profile("!smart")
public class EmployeeConfiguration {
	
	@Bean EmployeeService employeeService() {
		return new DefaultEmployeeService();
	}
}
