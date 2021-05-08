package hu.webuni.hr.gyd.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public Page<HolidayClaim> getAllPaged(Pageable page) {
		return claimRepository.findAll(page);
	}

	@Transactional
	public HolidayClaim createClaim(HolidayClaim holidayClaim, Long employeeId) {
		Employee claimant = employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchElementException());
		holidayClaim.setClaimant(claimant);
		holidayClaim.setPrincipal(null);		
		holidayClaim = claimRepository.save(holidayClaim);
		return holidayClaim;
	}
	
	@Transactional
	public HolidayClaim approveClaim(Long claimId, Long principalId) {
		HolidayClaim claim = claimRepository.findById(claimId).orElseThrow(() -> new NoSuchElementException());
		if(claim.getPrincipal() == null) {
			Employee principal = employeeRepository.findById(principalId).orElseThrow(() -> new NoSuchElementException());
			claim.setPrincipal(principal);			
		}		
		return claim;
	}

	@Transactional
	public HolidayClaim modifyClaim(HolidayClaim newClaim, Long employeeId, Long claimId) {
		if(!employeeRepository.existsById(employeeId)) throw new NoSuchElementException();
		HolidayClaim claim = claimRepository.findById(claimId).orElseThrow(() -> new NoSuchElementException());
		if(claim.getPrincipal() == null) {
			claim.setStart(newClaim.getStart());
			claim.setEnding(newClaim.getEnding());
			claim.setTimeOfApplication(newClaim.getTimeOfApplication());
		}
		return claim;
	}

	@Transactional
	public void deleteClaim(Long employeeId, Long claimId) {
		if(!employeeRepository.existsById(employeeId)) throw new NoSuchElementException();
		HolidayClaim claim = claimRepository.findById(claimId).orElseThrow(() -> new NoSuchElementException());
		if(claim.getPrincipal() == null) {
			claimRepository.deleteById(claimId);
		}
	}

}
