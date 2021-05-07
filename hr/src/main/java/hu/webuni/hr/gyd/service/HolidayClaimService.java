package hu.webuni.hr.gyd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.HolidayClaim;
import hu.webuni.hr.gyd.repository.HolidayClaimRepository;

@Service
public class HolidayClaimService {
	
	@Autowired
	HolidayClaimRepository claimRepository;

	public List<HolidayClaim> getAll() {
		return claimRepository.findAll();
	}
}
