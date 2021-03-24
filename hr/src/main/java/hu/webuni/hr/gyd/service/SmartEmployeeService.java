package hu.webuni.hr.gyd.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.configuration.HrConfigPropertiesWithLists;
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
	
	/*
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
	*/
	
	@Autowired
	HrConfigPropertiesWithLists config;
	
	private List<Double> limits;
	
	private List<Integer> percents;
	
	
	@Override
	public int getPayRaisePercent(Employee employee) {
		limits = config.getLimits().stream()
				.map(x -> Double.valueOf(x.toString()))
				.sorted(Comparator.reverseOrder())
				.collect(Collectors.toList());
		
		percents = config.getPercents().stream()
				.map(x -> (int) x)
				.sorted(Comparator.reverseOrder())
				.collect(Collectors.toList());
		
		int index = -1;
		for (Double limit : limits) {
			if(getYearsOfWork(employee.getHiringDate()) >= limit) {
				index = limits.indexOf(limit);
				break;
			}
		}
		return index == -1 ? config.getSmartDefault() : percents.get(index);
	}
	
	private double getYearsOfWork(LocalDateTime hiringDate) {
		return hiringDate.until(LocalDateTime.now(), ChronoUnit.DAYS) / 365d;
	}
}
