package hu.webuni.hr.gyd.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.CompanyType;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.repository.EmployeeRepository;

@Service
public class InitDbService {

	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	public void clearDB() {
		employeeRepository.deleteAll();
		companyRepository.deleteAll();
	}
	
	public void insertTestData() {
		Company c1 = new Company(1L, 12345, "IBM", "Pecs, Pecsi utca", CompanyType.BT);
		Company c2 = new Company(2L, 34567, "HP", "Szeged, Szegedi utca", CompanyType.KFT);
		Company c3 = new Company(3L, 56789, "Microsoft", "Szekszard, Szexardi utca", CompanyType.ZRT);
		Company c4 = new Company(4L, 45678, "Sun", "Gyor, Gyori utca", CompanyType.NYRT);
		
		companyRepository.saveAll(List.of(c1, c2, c3, c4));
		
		Employee e1 = new Employee(1L, "Kiss Jozsef", "Driver", 250000, LocalDateTime.of(2015, 2, 10, 0, 0), c1);
		Employee e2 = new Employee(2L, "Nagy Eva", "Manager", 350000, LocalDateTime.of(2010, 2, 10, 0, 0), c1);
		Employee e3 = new Employee(3L, "Szabo Imre", "Assistant", 250000, LocalDateTime.of(2019, 4, 10, 0, 0), c2);
		Employee e4 = new Employee(4L, "Nemeth Janos", "Teamleader", 450000, LocalDateTime.of(2010, 2, 10, 0, 0), c2);
		Employee e5 = new Employee(5L, "Magyar Geza", "Manager", 500000, LocalDateTime.of(2015, 2, 10, 0, 0), c2);
		Employee e6 = new Employee(6L, "Magyar Lajos", "Manager", 500000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e7 = new Employee(7L, "Horvath Ilona", "Teamleader", 450000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e8 = new Employee(8L, "Kovacs Eszter", "Manager", 350000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e9 = new Employee(9L, "Marton Bela", "Manager", 500000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e10 = new Employee(10L, "Adam Ferenc", "Driver", 250000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e11 = new Employee(11L, "Gajdos Bela", "Teamleader", 250000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e12 = new Employee(12L, "Zoltan Gabor", "Driver", 350000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		
		employeeRepository.saveAll(List.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12));
	}
}
