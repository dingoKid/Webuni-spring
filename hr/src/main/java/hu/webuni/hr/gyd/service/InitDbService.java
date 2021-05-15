package hu.webuni.hr.gyd.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.CompanyType;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HolidayClaim;
import hu.webuni.hr.gyd.model.HrUser;
import hu.webuni.hr.gyd.model.Position;
import hu.webuni.hr.gyd.model.Requirement;
import hu.webuni.hr.gyd.repository.CompanyRepository;
import hu.webuni.hr.gyd.repository.CompanyTypeRepository;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.HolidayClaimRepository;
import hu.webuni.hr.gyd.repository.HrUserRepository;
import hu.webuni.hr.gyd.repository.PositionRepository;

@Service
public class InitDbService {

	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CompanyTypeRepository companyTypeRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	HolidayClaimRepository holidayClaimRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	HrUserRepository userRepository;
	
	@Transactional
	public void clearDB() {
		employeeRepository.deleteAll();
		companyRepository.deleteAll();
		positionRepository.deleteAll();
		companyTypeRepository.deleteAll();
	}
	
	@Transactional
	public void insertTestData() {
		
		Position p1 = new Position(1L, "Driver", Requirement.NINCS);
		Position p2 = new Position(2L, "Manager", Requirement.FOISKOLA);
		Position p3 = new Position(3L, "Assistant", Requirement.ERETTSEGI);
		Position p4 = new Position(4L, "Teamleader", Requirement.EGYETEM);
		
		positionRepository.saveAll(List.of(p1, p2, p3, p4));
		
		CompanyType ct1 = new CompanyType(1L, "BT");
		CompanyType ct2 = new CompanyType(2L, "KFT");
		CompanyType ct3 = new CompanyType(3L, "ZRT");
		CompanyType ct4 = new CompanyType(4L, "NYRT");
		
		companyTypeRepository.saveAll(List.of(ct1, ct2, ct3, ct4));
		
		Company c1 = new Company(1L, 12345, "IBM", "Pecs, Pecsi utca", ct1);
		p1.setMinSalary(250000);
		p1.setCompany(c1);
		Company c2 = new Company(2L, 34567, "HP", "Szeged, Szegedi utca", ct2);
		Company c3 = new Company(3L, 56789, "Microsoft", "Szekszard, Szexardi utca", ct3);
		Company c4 = new Company(4L, 45678, "Sun", "Gyor, Gyori utca", ct4);
		
		companyRepository.saveAll(List.of(c1, c2, c3, c4));
		
		Employee e1 = new Employee(1L, "Kiss Jozsef", p1, 150000, LocalDateTime.of(2015, 2, 10, 0, 0), c1);
		Employee e2 = new Employee(2L, "Nagy Eva", p2, 350000, LocalDateTime.of(2010, 2, 10, 0, 0), c1);
		Employee e3 = new Employee(3L, "Szabo Imre", p3, 250000, LocalDateTime.of(2019, 4, 10, 0, 0), c2);
		Employee e4 = new Employee(4L, "Nemeth Janos", p4, 450000, LocalDateTime.of(2010, 2, 10, 0, 0), c2);
		Employee e5 = new Employee(5L, "Magyar Geza", p2, 500000, LocalDateTime.of(2015, 2, 10, 0, 0), c2);
		Employee e6 = new Employee(6L, "Magyar Lajos", p2, 500000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e7 = new Employee(7L, "Horvath Ilona", p4, 450000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e8 = new Employee(8L, "Kovacs Eszter", p2, 350000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e9 = new Employee(9L, "Marton Bela", p2, 500000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e10 = new Employee(10L, "Adam Ferenc", p1, 250000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e11 = new Employee(11L, "Gajdos Bela", p4, 250000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);
		Employee e12 = new Employee(12L, "Zoltan Gabor", p1, 350000, LocalDateTime.of(2015, 2, 10, 0, 0), c3);

		employeeRepository.saveAll(List.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12));
		
		e1.setUsername("kiss1");
		e2.setUsername("nagy1");
		e3.setUsername("szabo1");
		e4.setUsername("nemeth1");
		e5.setUsername("magyar1");
		e6.setUsername("magyar2");
		e7.setUsername("horvath1");
		e8.setUsername("kovacs1");
		e9.setUsername("marton1");
		e10.setUsername("adam1");
		e11.setUsername("gajdos1");
		e12.setUsername("zoltan1");
		
		e1.setPrincipal(e2);
		e2.setPrincipal(e2);
		e3.setPrincipal(e5);
		e4.setPrincipal(e5);
		e5.setPrincipal(e5);
		e6.setPrincipal(e6);
		e7.setPrincipal(e6);
		e8.setPrincipal(e6);
		e9.setPrincipal(e6);
		e10.setPrincipal(e6);
		e11.setPrincipal(e6);
		e12.setPrincipal(e6);
		
		
		HolidayClaim hc1 = new HolidayClaim(e1, LocalDate.of(2020, 1, 5), LocalDate.of(2020, 2, 10), LocalDate.of(2020, 2, 15));
		hc1.setPrincipal(e12);
		hc1.setApproved(true);
		HolidayClaim hc2 = new HolidayClaim(e2, LocalDate.of(2020, 2, 15), LocalDate.of(2020, 4, 10), LocalDate.of(2020, 4, 25));
		HolidayClaim hc3 = new HolidayClaim(e3, LocalDate.of(2020, 3, 25), LocalDate.of(2020, 3, 26), LocalDate.of(2020, 3, 28));
		hc3.setPrincipal(e12);
		hc3.setApproved(true);
		HolidayClaim hc4 = new HolidayClaim(e5, LocalDate.of(2020, 1, 5), LocalDate.of(2020, 2, 10), LocalDate.of(2020, 2, 15));
		hc4.setPrincipal(e12);
		hc4.setApproved(true);		
		HolidayClaim hc5 = new HolidayClaim(e6, LocalDate.of(2020, 11, 5), LocalDate.of(2020, 12, 10), LocalDate.of(2020, 12, 15));
		
		holidayClaimRepository.saveAll(List.of(hc1, hc2, hc3, hc4, hc5));
		
		HrUser hu1 = new HrUser("kiss1", passwordEncoder.encode("pass"), Set.of("user"));
		HrUser hu2 = new HrUser("nagy1", passwordEncoder.encode("pass"), Set.of("admin", "user"));
		
		userRepository.saveAll(List.of(hu1, hu2));
		
	}
}
