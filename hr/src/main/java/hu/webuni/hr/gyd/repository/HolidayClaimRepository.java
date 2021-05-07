package hu.webuni.hr.gyd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.gyd.model.HolidayClaim;

public interface HolidayClaimRepository extends JpaRepository<HolidayClaim, Long> {

}
