package hu.webuni.hr.gyd.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.configuration.HrConfigPropertiesWithLists;
import hu.webuni.hr.gyd.model.Employee;

@Service
public class SmartEmployeeService extends AbstractEmployeeService {
		
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
