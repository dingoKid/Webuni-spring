package hu.webuni.hr.gyd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import hu.webuni.hr.gyd.model.HolidayClaim;

public interface HolidayClaimRepository extends JpaRepository<HolidayClaim, Long>, JpaSpecificationExecutor<HolidayClaim> {

}
