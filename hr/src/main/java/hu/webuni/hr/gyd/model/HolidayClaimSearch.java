package hu.webuni.hr.gyd.model;

import java.time.LocalDate;

public class HolidayClaimSearch {
	
//	private Long claimNumber;
	private Employee claimant;
	private Employee principal;
	private LocalDate startOfApplication;
	private LocalDate endOfApplication;
	private LocalDate start;
	private LocalDate ending;
	private boolean isApproved;
	
	public HolidayClaimSearch() {}
	
//	public HolidayClaimSearchDto(String claimant, LocalDate timeOfApplication, LocalDate start, LocalDate ending) {
//		this.claimant = claimant;
//		this.timeOfApplication = timeOfApplication;
//		this.start = start;
//		this.ending = ending;
//	}

//	public Long getClaimNumber() {
//		return claimNumber;
//	}
//
//	public void setClaimNumber(Long claimNumber) {
//		this.claimNumber = claimNumber;
//	}
	
	


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

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	
	

}
