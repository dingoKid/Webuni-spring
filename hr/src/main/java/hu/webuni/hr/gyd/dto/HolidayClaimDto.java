package hu.webuni.hr.gyd.dto;

import java.time.LocalDate;

public class HolidayClaimDto {
	
	private String claimant;
	private String principal;
	private LocalDate timeOfApplication;
	private LocalDate start;
	private LocalDate ending;
	private boolean isApproved;
	
	public HolidayClaimDto(String claimant, LocalDate timeOfApplication, LocalDate start, LocalDate ending) {
		this.claimant = claimant;
		this.timeOfApplication = timeOfApplication;
		this.start = start;
		this.ending = ending;
	}

	public String getClaimant() {
		return claimant;
	}

	public void setClaimant(String claimant) {
		this.claimant = claimant;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
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
