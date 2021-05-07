package hu.webuni.hr.gyd.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class HolidayClaim {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Employee claimant;
	private Long claimantId;
	
//	@ManyToOne
//	private Employee principal;
//	private Long principalId;
//	
	private LocalDate timeOfApplication;
	private LocalDate start;
	private LocalDate ending;
	private boolean isApproved;
		

	public HolidayClaim() {	}


	public HolidayClaim(Long id, Employee claimant, Long claimantId, LocalDate timeOfApplication, LocalDate start,
			LocalDate ending, boolean isApproved) {
		super();
		this.id = id;
		this.claimant = claimant;
		this.claimantId = claimantId;
		this.timeOfApplication = timeOfApplication;
		this.start = start;
		this.ending = ending;
		this.isApproved = isApproved;
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


	public Long getClaimantId() {
		return claimantId;
	}


	public void setClaimantId(Long claimantId) {
		this.claimantId = claimantId;
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


	public boolean isApproved() {
		return isApproved;
	}


	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	
	
}
