package hu.webuni.hr.gyd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.gyd.model.HrUser;

public interface HrUserRepository extends JpaRepository<HrUser, String>{

}
