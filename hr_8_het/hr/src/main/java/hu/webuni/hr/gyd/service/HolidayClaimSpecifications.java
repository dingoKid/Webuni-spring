package hu.webuni.hr.gyd.service;

import java.time.LocalDate;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Employee_;
import hu.webuni.hr.gyd.model.HolidayClaim;
import hu.webuni.hr.gyd.model.HolidayClaim_;

public class HolidayClaimSpecifications {

	public static Specification<HolidayClaim> hasClaimant(Employee claimant) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(HolidayClaim_.claimant).get(Employee_.name)), claimant.getName().toLowerCase() + "%");
	}

	public static Specification<HolidayClaim> hasPrincipal(Employee principal) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(HolidayClaim_.principal).get(Employee_.name)), principal.getName().toLowerCase() + "%");
	}

	public static Specification<HolidayClaim> hasStartAndEndOfApplication(LocalDate startOfApplication,
			LocalDate endOfApplication) {
		return (root, cq, cb) -> cb.between(root.get(HolidayClaim_.timeOfApplication), startOfApplication, endOfApplication);
	}

	public static Specification<HolidayClaim> hasStartAndEndOfHoliday(LocalDate start, LocalDate ending) {
		return (root, cq, cb) -> {
			Predicate first = cb.lessThanOrEqualTo(root.get(HolidayClaim_.start), ending);
			Predicate second = cb.greaterThanOrEqualTo(root.get(HolidayClaim_.ending), start);
			Predicate intervalsOverlap = cb.and(first, second);
			return intervalsOverlap;
		};
	}

	public static Specification<HolidayClaim> hasApproval(Boolean isApproved) {
		return (root, cq, cb) -> cb.equal(root.get(HolidayClaim_.isApproved), isApproved);
	}
	
	

}
