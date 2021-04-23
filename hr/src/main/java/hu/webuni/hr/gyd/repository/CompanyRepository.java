package hu.webuni.hr.gyd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.webuni.hr.gyd.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
