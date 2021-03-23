package hu.webuni.hr.gyd.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.configuration.HrConfigProperties;
import hu.webuni.hr.gyd.model.Employee;

@Service
public class SmartEmployeeService implements EmployeeService {
	
	/*
	@Value("${hr.employee.smart.junior.limit}")
	private double juniorLimit;
	
	@Value("${hr.employee.smart.medior.limit}")
	private double mediorLimit;
	
	@Value("${hr.employee.smart.senior.limit}")
	private double seniorLimit;
	
	
	@Value("${hr.employee.smart.default.percent}")
	private int defaultPercent;
	
	@Value("${hr.employee.smart.junior.percent}")
	private int juniorPercent;
	
	@Value("${hr.employee.smart.medior.percent}")
	private int mediorPercent;
	
	@Value("${hr.employee.smart.senior.percent}")
	private int seniorPercent;
	*/
	
	@Autowired
	HrConfigProperties config;

	@Override
	public int getPayRaisePercent(Employee employee) {
		if(getYearsOfWork(employee.getHiringDate()) >= config.getEmployee().getSmart().getSenior().getLimit()) 
			return config.getEmployee().getSmart().getSenior().getPercent();
		if(getYearsOfWork(employee.getHiringDate()) >= config.getEmployee().getSmart().getMedior().getLimit()
				&& getYearsOfWork(employee.getHiringDate()) < config.getEmployee().getSmart().getSenior().getLimit())
			return config.getEmployee().getSmart().getMedior().getPercent();
		if(getYearsOfWork(employee.getHiringDate()) >= config.getEmployee().getSmart().getJunior().getLimit()
				&& getYearsOfWork(employee.getHiringDate()) < config.getEmployee().getSmart().getMedior().getLimit())
			return config.getEmployee().getSmart().getJunior().getPercent();
		return config.getEmployee().getSmart().getSmartDefault().getPercent();
	}

	private double getYearsOfWork(LocalDateTime hiringDate) {
		return hiringDate.until(LocalDateTime.now(), ChronoUnit.DAYS) / 365d;
	}
}
