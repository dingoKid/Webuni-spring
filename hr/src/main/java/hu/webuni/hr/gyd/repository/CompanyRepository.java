package hu.webuni.hr.gyd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hu.webuni.hr.gyd.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	@Query("select distinct c from Company c join Employee e on c.companyId = e.company.id where e.salary > ?1 order by c.companyId")
	List<Company> findBySalary(int salary);
	
	
		
}
