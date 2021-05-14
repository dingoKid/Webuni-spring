package hu.webuni.hr.gyd.web;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.mapper.HolidayClaimMapper;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HolidayClaim;
import hu.webuni.hr.gyd.model.HolidayClaimSearch;
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
	public HolidayClaimDto createClaim(@RequestBody HolidayClaimDto claim, @PathVariable Long employeeId) throws NoSuchElementException {
		HolidayClaim holidayClaim = holidayClaimMapper.dtoToClaim(claim);
//		try {
			return holidayClaimMapper.claimToDto(holidayClaimService.createClaim(holidayClaim, employeeId));
//		} catch (NoSuchElementException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND); 
//		}
	}
	
	@GetMapping("/approve/{claimId}/{principalId}")
	public HolidayClaimDto approveClaim(@PathVariable Long claimId, @PathVariable Long principalId) throws NoSuchElementException {
//		try {
			return holidayClaimMapper.claimToDto(holidayClaimService.approveClaim(claimId, principalId));
//		} catch (NoSuchElementException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//		}
	}
	
	@PutMapping("/modify/{claimId}")
	public HolidayClaimDto modifyClaim(@RequestBody HolidayClaimDto claim, @PathVariable Long claimId) throws NoSuchElementException {
		HolidayClaim holidayClaim = holidayClaimMapper.dtoToClaim(claim);
//		try {
			return holidayClaimMapper.claimToDto(holidayClaimService.modifyClaim(holidayClaim, claimId));
//		} catch (NoSuchElementException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//		}
	}
	
	@DeleteMapping("/delete/{claimId}")
	public void deleteClaim(@PathVariable Long claimId) {
//		try {
			holidayClaimService.deleteClaim(claimId);
//		} catch (NoSuchElementException e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//		}
	}
	
	@GetMapping("/search")
	public List<HolidayClaimDto> searchClaims(
				@RequestParam(required = false) Boolean approved,
				@RequestParam(required = false) String claimant,
				@RequestParam(required = false) String principal,
				@RequestParam(required = false) LocalDate applicationStart,
				@RequestParam(required = false) LocalDate applicationEnd,
				@RequestParam(required = false) LocalDate holidayStart,
				@RequestParam(required = false) LocalDate holidayEnd) {
		HolidayClaimSearch example = new HolidayClaimSearch();
		
		if(claimant != null) {
			example.setClaimant(new Employee());
			example.getClaimant().setName(claimant);
		}

		if(principal != null) {
			example.setPrincipal(new Employee());
			example.getPrincipal().setName(principal);
		}
		
		if(applicationStart != null && applicationEnd == null)
			applicationEnd = LocalDate.now();

		if(applicationStart == null && applicationEnd != null)
			applicationStart = LocalDate.now().minusYears(50);
		
		if(applicationStart != null && applicationEnd != null && applicationStart.isAfter(applicationEnd)) {
			LocalDate temp = applicationStart;
			applicationStart = applicationEnd;
			applicationEnd = temp;
		}
		
		if(holidayStart != null && holidayEnd == null)
			holidayEnd = LocalDate.now();

		if(holidayStart == null && holidayEnd != null)
			holidayStart = LocalDate.now().minusYears(50);
		
		if(holidayStart != null && holidayEnd != null && holidayStart.isAfter(holidayEnd)) {
			LocalDate temp = holidayStart;
			holidayStart = holidayEnd;
			holidayEnd = temp;
		}
		
		example.setApproved(approved);		
		example.setStartOfApplication(applicationStart);
		example.setEndOfApplication(applicationEnd);
		example.setStart(holidayStart);
		example.setEnding(holidayEnd);
		
		List<HolidayClaim> searchResult = holidayClaimService.findByExample(example);		
		return holidayClaimMapper.claimsToDtos(searchResult);
	}
	
	
	
}
