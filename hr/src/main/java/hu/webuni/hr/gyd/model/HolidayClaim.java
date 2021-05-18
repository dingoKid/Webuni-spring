package hu.webuni.hr.gyd.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class HolidayClaim {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Employee claimant;
	
	@ManyToOne
	private Employee principal;
	
	private LocalDate timeOfApplication;
	private LocalDate start;
	private LocalDate ending;
	private Boolean isApproved;
		

	public HolidayClaim() {	}


	public HolidayClaim(Employee claimant, LocalDate timeOfApplication, LocalDate start, LocalDate ending) {
		this.claimant = claimant;
		this.start = start;
		this.ending = ending;
		this.timeOfApplication = timeOfApplication;
		this.isApproved = false;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Employee getClaimant() {
		return claimant;
	}


	public void setClaimant(Employee claimant) {
		this.claimant = claimant;
	}
	
	public Employee getPrincipal() {
		return principal;
	}


	public void setPrincipal(Employee principal) {
		if(principal == null) {
			this.principal = null;
			this.setIsApproved(false);
		}
		else {
			this.principal = principal;
			this.setIsApproved(true);
		}
	}


	public LocalDate getTimeOfApplication() {
		return timeOfApplication;
	}


	public void setTimeOfApplication(LocalDate timeOfApplication) {
		this.timeOfApplication = timeOfApplication;
	}


	public LocalDate getStart() {
		return start;
	}


	public void setStart(LocalDate start) {
		this.start = start;
	}


	public LocalDate getEnding() {
		return ending;
	}


	public void setEnding(LocalDate ending) {
		this.ending = ending;
	}


	public Boolean getIsApproved() {
		return this.principal == null ? false : true;
	}


	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}
	
	
}
