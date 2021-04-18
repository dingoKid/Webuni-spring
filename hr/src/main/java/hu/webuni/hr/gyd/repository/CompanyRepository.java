package hu.webuni.hr.gyd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.gyd.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
