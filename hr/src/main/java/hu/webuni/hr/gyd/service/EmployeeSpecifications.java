package hu.webuni.hr.gyd.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.gyd.model.Company_;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Employee_;
import hu.webuni.hr.gyd.model.Position;
import hu.webuni.hr.gyd.model.Position_;

public class EmployeeSpecifications {

	public static Specification<Employee> hasId(Long id) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.employeeId), id);
	}

	public static Specification<Employee> hasName(String name) {
		return (root, cq, cb) -> cb.like(root.get(Employee_.name), name + "%");
	}

	public static Specification<Employee> hasPosition(String position) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.position).get(Position_.name), position);
	}

	/*public static Specification<Employee> hasSalary(int salary) {
		Double over = salary * 0.95;
		Double under = salary * 1.05;
		return (root, cq, cb) -> cb.between(root.get(Employee_.salary), 1, 2);
	}*/

	public static Specification<Employee> hasHiringDate(LocalDateTime hiringDate) {
		LocalDateTime start = LocalDateTime.of(hiringDate.toLocalDate(), LocalTime.of(0, 0));
		return (root, cq, cb) -> cb.between(root.get(Employee_.hiringDate), start, start.plusDays(1));
	}

	public static Specification<Employee> hasCompany(String companyName) {
		return (root, cq, cb) -> cb.like(root.get(Employee_.company).get(Company_.name), companyName + "%");
	}

}
