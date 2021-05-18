package hu.webuni.hr.gyd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.webuni.hr.gyd.model.CompanyType;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {
	
	CompanyType findByName(String name);
		
}
