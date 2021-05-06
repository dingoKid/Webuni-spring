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
	
	@ManyToOne
	private Employee principal;
	
	private LocalDate timeOfApplication;
	private LocalDate start;
	private LocalDate end;
	private boolean isApproved;
		
	public HolidayClaim() {	}
	
	public HolidayClaim(Employee claimant, LocalDate timeOfApplication, LocalDate start, LocalDate end) {
		this.claimant = claimant;
		this.timeOfApplication = timeOfApplication;
		this.start = start;
		this.end = end;
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
		this.principal = principal;
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
	public LocalDate getEnd() {
		return end;
	}
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	public boolean isApproved() {
		return isApproved;
	}
	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	
	
	
}
