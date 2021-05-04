package hu.webuni.hr.gyd.service;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.gyd.model.Employee;

public class EmployeeSpecifications {

	public static Specification<Employee> hasId(Long id) {
		return (root, cq, cb) -> cb.equal(root.get("id"), id);
	}

}
