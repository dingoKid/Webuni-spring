package hu.webuni.hr.gyd.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.mapper.HolidayClaimMapper;
import hu.webuni.hr.gyd.model.HolidayClaim;
import hu.webuni.hr.gyd.service.HolidayClaimService;

@RestController
@RequestMapping("api/holidays")
public class HolidayClaimController {
	
	@Autowired
	HolidayClaimService holidayClaimService;
	
	@Autowired
	HolidayClaimMapper holidayClaimMapper;

	@PostMapping("/{id}")
	public HolidayClaimDto createClaim(@RequestBody @Valid HolidayClaimDto claimDto, @PathVariable long claimantId ) {
//		HolidayClaim claim = holidayClaimMapper.DtoToClaim(claimDto);
//		try {
//			return holidayClaimService.createClaim(claim, claimantId);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		return null;
	}
}
