package hu.webuni.hr.gyd.model;

import java.time.LocalDate;

public class HolidayClaimSearch {
	
	private Employee claimant;
	private Employee principal;
	private LocalDate startOfApplication;
	private LocalDate endOfApplication;
	private LocalDate start;
	private LocalDate ending;
	private Boolean isApproved;
	
	public HolidayClaimSearch() {}

	public LocalDate getStartOfApplication() {
		return startOfApplication;
	}

	public void setStartOfApplication(LocalDate startOfApplication) {
		this.startOfApplication = startOfApplication;
	}

	public LocalDate getEndOfApplication() {
		return endOfApplication;
	}

	public void setEndOfApplication(LocalDate endOfApplication) {
		this.endOfApplication = endOfApplication;
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

	public Boolean isApproved() {
		return isApproved;
	}

	public void setApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}
	
	

}
