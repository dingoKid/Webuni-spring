package hu.webuni.hr.gyd.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.mapper.HolidayClaimMapper;
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
}
