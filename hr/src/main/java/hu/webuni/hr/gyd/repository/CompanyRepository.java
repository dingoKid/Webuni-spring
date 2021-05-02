package hu.webuni.hr.gyd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hu.webuni.hr.gyd.model.Company;
import hu.webuni.hr.gyd.model.PositionSalary;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	@Query("select distinct c from Company c left join fetch c.employees where c.companyId = :id")
	Company findByIdWithEmployees(long id);
	
	@Query("select distinct c from Company c left join fetch c.employees")
	List<Company> findAllWithEmployees();
	
	@Query("select distinct c from Company c join Employee e on c.companyId = e.company.id where e.salary > :salary order by c.companyId")
	List<Company> findBySalary(int salary);
	
	@Query("select e.position.name as position, avg(e.salary) as averageSalary from Employee e where e.company.id = :id group by position order by avg(e.salary) desc")
	List<PositionSalary> findSalariesById(long id);
	
	@Query("select c from Company c where size(c.employees) > :number")
	List<Company> findByEmployeesNumber(int number);
	
}
