package hu.webuni.hr.gyd.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hu.webuni.hr.gyd.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("select e from Employee e where e.position = ?1")
	List<Employee> findByPosition(String position);
	
	List<Employee> findByNameStartingWithIgnoreCase(String name);
	
	//@Query("select e from Employee e where e.hiringDate between ?1 and ?2")
	List<Employee> findByHiringDateBetween(LocalDateTime start, LocalDateTime end);
}
