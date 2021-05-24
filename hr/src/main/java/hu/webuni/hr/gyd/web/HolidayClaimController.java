package hu.webuni.hr.gyd.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.dto.HolidayClaimSearchDto;
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
	public List<HolidayClaimDto> getAllHolidays(Pageable page) {
		return holidayClaimMapper.claimsToDtos(page == null ? holidayClaimService.getAll() 
															: holidayClaimService.getAllPaged(page).getContent());
	}
		
	@PostMapping
	public HolidayClaimDto createClaim(@RequestBody HolidayClaimDto claim) {
		HolidayClaim holidayClaim = holidayClaimMapper.dtoToClaim(claim);
			return holidayClaimMapper.claimToDto(holidayClaimService.createClaim(holidayClaim));
	}
	
	@PutMapping("/modify/{claimId}")
	public HolidayClaimDto modifyClaim(@RequestBody HolidayClaimDto claim, @PathVariable Long claimId) {
		HolidayClaim holidayClaim = holidayClaimMapper.dtoToClaim(claim);
			return holidayClaimMapper.claimToDto(holidayClaimService.modifyClaim(holidayClaim, claimId));
	}
	
	@DeleteMapping("/delete/{claimId}")
	public Long deleteClaim(@PathVariable Long claimId) {
		return holidayClaimService.deleteClaim(claimId);
	}
	
	@GetMapping("/approve/{claimId}")
	public HolidayClaimDto approveClaim(@PathVariable Long claimId) {
		return holidayClaimMapper.claimToDto(holidayClaimService.approveClaim(claimId));
	}
	
	@PostMapping("/search")
	public List<HolidayClaimDto> searchClaims(@RequestBody HolidayClaimSearchDto example) {
		
		if(example.getStartOfApplication() != null && example.getEndOfApplication() == null)
			example.setEndOfApplication(LocalDate.now());

		if(example.getStartOfApplication() == null && example.getEndOfApplication() != null)
			example.setStartOfApplication(LocalDate.now().minusYears(50));
		
		if(example.getStartOfApplication() != null && example.getEndOfApplication() != null && example.getStartOfApplication().isAfter(example.getEndOfApplication())) {
			LocalDate temp = example.getStartOfApplication();
			example.setStartOfApplication(example.getEndOfApplication());
			example.setEndOfApplication(temp);
		}
		
		if(example.getStart() != null && example.getEnding() == null)
			example.setEnding(LocalDate.now());

		if(example.getStart() == null && example.getEnding() != null)
			example.setStart(LocalDate.now().minusYears(50));
		
		if(example.getStart() != null && example.getEnding() != null && example.getStart().isAfter(example.getEnding())) {
			LocalDate temp = example.getStart();
			example.setStart(example.getEnding());
			example.setEnding(temp);
		}
		
		List<HolidayClaim> searchResult = holidayClaimService.findByExample(example);
		return holidayClaimMapper.claimsToDtos(searchResult);
	}
	
}
