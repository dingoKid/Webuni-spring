package hu.webuni.hr.gyd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.configuration.HrConfigPropertiesWithLists;
import hu.webuni.hr.gyd.model.Employee;

@Service
public class DefaultEmployeeService extends EmployeeServices {

	/*
	@Value("${hr.employee.def.percent}")
	private int defaultPercent;
	*/
	
	/*
	@Autowired
	HrConfigProperties config;
	*/
	
	@Autowired
	HrConfigPropertiesWithLists config;
	
	@Override
	public int getPayRaisePercent(Employee employee) {
		//return config.getEmployee().getDef().getPercent();
		return config.getDef();
	}

}
