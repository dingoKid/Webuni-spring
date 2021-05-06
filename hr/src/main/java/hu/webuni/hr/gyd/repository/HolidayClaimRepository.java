package hu.webuni.hr.gyd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import hu.webuni.hr.gyd.model.HolidayClaim;

@Repository
public interface HolidayClaimRepository extends JpaRepository<HolidayClaim, Long>, JpaSpecificationExecutor<HolidayClaim> {

}
