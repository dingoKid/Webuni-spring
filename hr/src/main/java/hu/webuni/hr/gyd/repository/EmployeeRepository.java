package hu.webuni.hr.gyd.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.Position;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	//@Query("select e from Employee e where e.position = ?1")
	Page<Employee> findByPosition(Position position, Pageable pageable);
	
	List<Employee> findByNameStartingWithIgnoreCase(String name);
	
	List<Employee> findByHiringDateBetween(LocalDateTime start, LocalDateTime end);
	
	List<Employee> findByCompanyAndPosition(Company company, Position position);
	
	Optional<Employee> findById(Long id);
	
	
	
}
