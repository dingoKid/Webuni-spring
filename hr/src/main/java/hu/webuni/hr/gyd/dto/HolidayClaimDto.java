package hu.webuni.hr.gyd.dto;

import java.time.LocalDate;

public class HolidayClaimDto {
	
	private Long claimNumber;
	private String claimant;
	private String principal;
	private LocalDate timeOfApplication;
	private LocalDate start;
	private LocalDate ending;
	private Boolean isApproved;
	
	public HolidayClaimDto() {}
	
	public HolidayClaimDto(LocalDate timeOfApplication, LocalDate start, LocalDate ending) {
		this.timeOfApplication = timeOfApplication;
		this.start = start;
		this.ending = ending;
		isApproved = false;
	}

	public Long getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(Long claimNumber) {
		this.claimNumber = claimNumber;
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
		if(principal == null) {
			this.principal = null;
			this.setApproved(false);
		}
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

	public Boolean isApproved() {
		return isApproved;
	}

	public void setApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((claimNumber == null) ? 0 : claimNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HolidayClaimDto other = (HolidayClaimDto) obj;
		if (claimNumber == null) {
			if (other.claimNumber != null)
				return false;
		} else if (!claimNumber.equals(other.claimNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HolidayClaimDto [claimNumber=" + claimNumber + ", claimant=" + claimant + ", principal=" + principal
				+ ", timeOfApplication=" + timeOfApplication + ", start=" + start + ", ending=" + ending
				+ ", isApproved=" + isApproved + "]";
	}
	
	

}
