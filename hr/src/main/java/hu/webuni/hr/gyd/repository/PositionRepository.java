package hu.webuni.hr.gyd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hu.webuni.hr.gyd.model.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
	
	@Query("select p from Position p where lower(p.name) = lower(:name)")
	Optional<Position> findByName(String name);
		
}
