package hu.webuni.hr.gyd.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HolidayClaim;
import hu.webuni.hr.gyd.model.HolidayClaimSearch;
import hu.webuni.hr.gyd.model.HrUserDetails;
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
	public HolidayClaim createClaim(HolidayClaim holidayClaim) {
		Long authenticatedUserId = ((HrUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmployee().getEmployeeId();
		Employee claimant = employeeRepository.findById(authenticatedUserId).get();
		holidayClaim.setClaimant(claimant);
		holidayClaim.setPrincipal(null);		
		holidayClaim = claimRepository.save(holidayClaim);
		return holidayClaim;
	}
	
	@Transactional
	public HolidayClaim approveClaim(Long claimId) {
		Long authenticatedUserId = ((HrUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmployee().getEmployeeId();
		HolidayClaim claim = claimRepository.findById(claimId).orElseThrow(() -> new NoSuchElementException("No claim found with id: " + claimId));
		if(claim.getPrincipal() != null) {
			throw new ClaimAlreadyApprovedException("Claim is already approved");
		}	
		Long claimantPrincipalId = claim.getClaimant().getPrincipal().getEmployeeId();
		if(claimantPrincipalId.equals(authenticatedUserId))
		{
			Employee principal = employeeRepository.findById(authenticatedUserId).get();
			claim.setPrincipal(principal);
		}
		return claim;
	}

	@Transactional
	public HolidayClaim modifyClaim(HolidayClaim newClaim, Long claimId) {
		Long authenticatedUserId = ((HrUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmployee().getEmployeeId();
		HolidayClaim claim = claimRepository.findById(claimId).orElseThrow(() -> new NoSuchElementException("No claim found with id: " + claimId));
		
		if(claim.getPrincipal() != null) {
			throw new ClaimAlreadyApprovedException("Approved claims can not be modified");
		}
		
		if(claim.getClaimant().getEmployeeId() == authenticatedUserId) {
			claim.setStart(newClaim.getStart());
			claim.setEnding(newClaim.getEnding());
			claim.setTimeOfApplication(newClaim.getTimeOfApplication());
		}
		return claim;
	}

	@Transactional
	public Long deleteClaim(Long claimId) {
		Long authenticatedUserId = ((HrUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmployee().getEmployeeId();
		HolidayClaim claim = claimRepository.findById(claimId).orElseThrow(() -> new NoSuchElementException("No claim found with id: " + claimId));
		if(claim.getPrincipal() != null) {
			throw new ClaimAlreadyApprovedException("Approved claims can not be deleted");
		}
		if(claim.getClaimant().getEmployeeId() == authenticatedUserId) {
			claimRepository.deleteById(claimId);
		}
		return claim.getClaimant().getEmployeeId();
	}

	public List<HolidayClaim> findByExample(HolidayClaimSearch example) {
		String claimant = example.getClaimant();
		String principal = example.getPrincipal();
		
		LocalDate startOfApplication = example.getStartOfApplication();
		LocalDate endOfApplication = example.getEndOfApplication();
		LocalDate start = example.getStart();
		LocalDate ending = example.getEnding();
		Boolean isApproved = example.isApproved();
		
		Specification<HolidayClaim> spec = Specification.where(null);
		
		if(claimant != null) {
			employeeRepository.findByNameStartingWithIgnoreCase(claimant)
			.orElseThrow(() -> new NoSuchElementException("No employee found with name: " + claimant));
			
			spec = spec.and(HolidayClaimSpecifications.hasClaimant(claimant));
		}

		if(principal != null) {
			employeeRepository.findByNameStartingWithIgnoreCase(principal)
			.orElseThrow(() -> new NoSuchElementException("No employee found with name: " + principal));
			
			spec = spec.and(HolidayClaimSpecifications.hasPrincipal(principal));
		}
		
		if(startOfApplication != null) {
			spec = spec.and(HolidayClaimSpecifications.hasStartAndEndOfApplication(startOfApplication, endOfApplication));
		}
		
		if(start != null) {
			spec = spec.and(HolidayClaimSpecifications.hasStartAndEndOfHoliday(start, ending));
		}
		
		if(isApproved != null) {
			spec = spec.and(HolidayClaimSpecifications.hasApproval(isApproved));
		}
		
		return claimRepository.findAll(spec);
	}

}
