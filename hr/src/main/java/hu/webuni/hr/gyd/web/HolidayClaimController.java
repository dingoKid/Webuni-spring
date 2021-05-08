package hu.webuni.hr.gyd.web;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.mapper.HolidayClaimMapper;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HolidayClaim;
import hu.webuni.hr.gyd.service.HolidayClaimService;

@RestController
@RequestMapping("api/holidays")
public class HolidayClaimController {
	
	@Autowired
	HolidayClaimMapper holidayClaimMapper;
	
	@Autowired
	HolidayClaimService holidayClaimService;

	@GetMapping
	public List<HolidayClaimDto> getAllHolidays(Pageable page) {
		return holidayClaimMapper.claimsToDtos(page == null ? holidayClaimService.getAll() 
															: holidayClaimService.getAllPaged(page).getContent());
	}
	
	@PostMapping("/{employeeId}")
	public HolidayClaimDto createClaim(@RequestBody HolidayClaimDto claim, @PathVariable Long employeeId) {
		HolidayClaim holidayClaim = holidayClaimMapper.dtoToClaim(claim);
		try {
			return holidayClaimMapper.claimToDto(holidayClaimService.createClaim(holidayClaim, employeeId));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); 
		}
	}
	
	@GetMapping("/approve/{claimId}/{principalId}")
	public HolidayClaimDto approveClaim(@PathVariable Long claimId, @PathVariable Long principalId) {
		try {
			return holidayClaimMapper.claimToDto(holidayClaimService.approveClaim(claimId, principalId));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/modify/{employeeId}/{claimId}")
	public HolidayClaimDto modifyClaim(@RequestBody HolidayClaimDto claim, @PathVariable Long employeeId, @PathVariable Long claimId) {
		HolidayClaim holidayClaim = holidayClaimMapper.dtoToClaim(claim);
		try {
			return holidayClaimMapper.claimToDto(holidayClaimService.modifyClaim(holidayClaim, employeeId, claimId));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/delete/{employeeId}/{claimId}")
	public void deleteClaim(@PathVariable Long employeeId, @PathVariable Long claimId) {
		try {
			holidayClaimService.deleteClaim(employeeId, claimId);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/search")
	public List<HolidayClaimDto> searchClaims(
				@RequestParam(required = false) boolean approved,
				@RequestParam(required = false) String claimant,
				@RequestParam(required = false) String principal,
				@RequestParam(required = false) LocalDate applicationStart,
				@RequestParam(required = false) LocalDate applicationEnd,
				@RequestParam(required = false) LocalDate holidayStart,
				@RequestParam(required = false) LocalDate holidayEnd) {
		HolidayClaim example = new HolidayClaim();
		example.setApproved(approved);
		
		if(claimant != null) {
			example.setClaimant(new Employee());
			example.getClaimant().setName(claimant);
		}

		if(principal != null) {
			example.setPrincipal(new Employee());
			example.getPrincipal().setName(principal);
		}
		
		if(applicationStart.isAfter(applicationEnd)) {
			LocalDate temp = applicationStart;
			applicationStart = applicationEnd;
			applicationEnd = temp;
		}
		
		return null;
	}
	
	
	
}
