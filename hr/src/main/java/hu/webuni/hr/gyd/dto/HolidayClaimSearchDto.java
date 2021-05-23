package hu.webuni.hr.gyd.dto;

import java.time.LocalDate;

public class HolidayClaimSearchDto {
	
	private String claimant;
	private String principal;
	private LocalDate startOfApplication;
	private LocalDate endOfApplication;
	private LocalDate start;
	private LocalDate ending;
	private Boolean isApproved;
	
	public HolidayClaimSearchDto() {}

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
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

}
