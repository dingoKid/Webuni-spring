package hu.webuni.hr.gyd.web;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.mapper.HolidayClaimMapper;
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
	public List<HolidayClaimDto> getAllHolidays() {
		return holidayClaimMapper.claimsToDtos(holidayClaimService.getAll());
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
	
	
	
	
}
