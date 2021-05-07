package hu.webuni.hr.gyd.web;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/{id}")
	public HolidayClaimDto createClaim(@RequestBody HolidayClaimDto claim, @PathVariable Long id) {
		HolidayClaim holidayClaim = holidayClaimMapper.dtoToClaim(claim);
		try {
			return holidayClaimMapper.claimToDto(holidayClaimService.createClaim(holidayClaim, id));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); 
		}
	}
}
