package hu.webuni.hr.gyd.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HolidayClaim;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.HolidayClaimRepository;

@Service
public class HolidayClaimService {
	
	@Autowired
	HolidayClaimRepository claimRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;

	public List<HolidayClaim> getAll() {
		return claimRepository.findAll();
	}

	@Transactional
	public HolidayClaim createClaim(HolidayClaim holidayClaim, Long id) {
		Employee claimant = employeeRepository.findById(id).orElseThrow(() -> new NoSuchElementException());		
		holidayClaim.setClaimant(claimant);
		holidayClaim.setPrincipal(null);		
		holidayClaim = claimRepository.save(holidayClaim);
		return holidayClaim;
	}
}
