package hu.webuni.hr.gyd.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hu.webuni.hr.gyd.dto.PositionSalaryDto;
import hu.webuni.hr.gyd.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	//@Query("select e from Employee e where e.position = ?1")
	Page<Employee> findByPosition(String position, Pageable pageable);
	
	List<Employee> findByNameStartingWithIgnoreCase(String name);
	
	List<Employee> findByHiringDateBetween(LocalDateTime start, LocalDateTime end);
	
	@Query("select e.position as position, avg(e.salary) as averageSalary from Employee e where e.company.id = ?1 group by e.position order by avg(e.salary) desc")
	List<PositionSalaryDto> findSalariesById(long id);
	
	
}
