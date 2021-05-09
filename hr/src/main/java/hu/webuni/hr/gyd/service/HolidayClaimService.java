package hu.webuni.hr.gyd.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HolidayClaim;
import hu.webuni.hr.gyd.model.HolidayClaimSearch;
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

	public List<HolidayClaim> findByExample(HolidayClaimSearch example) {
		Employee claimant = example.getClaimant();
		Employee principal = example.getPrincipal();
		LocalDate startOfApplication = example.getStartOfApplication();
		LocalDate endOfApplication = example.getEndOfApplication();
		LocalDate start = example.getStart();
		LocalDate ending = example.getEnding();
		Boolean isApproved = example.isApproved();
		
		Specification<HolidayClaim> spec = Specification.where(null);
		
		if(claimant != null) {
			spec = spec.and(HolidayClaimSpecifications.hasClaimant(claimant));
		}

		if(principal != null) {
			spec = spec.and(HolidayClaimSpecifications.hasPrincipal(principal));
		}
		
		if(startOfApplication != null) {
			spec = spec.and(HolidayClaimSpecifications.hasStartAndEndOfApplication(startOfApplication, endOfApplication));
		}
		
		if(start != null) {
			spec = spec.and(HolidayClaimSpecifications.hasStartAndEndOfApplicationHoliday(start, ending));
		}
		
		if(isApproved != null) {
			spec = spec.and(HolidayClaimSpecifications.hasApproval(isApproved));
		}
		
		return claimRepository.findAll(spec);
	}

}
