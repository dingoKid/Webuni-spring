package hu.webuni.hr.gyd.dto;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;

public class HolidayClaimDto {

	private Long id;
	
	@NotBlank
	private String claimant;
	
	private String principal;
	
	private LocalDate timeOfApplication;
	
	@Future
	private LocalDate start;
	
	@Future
	private LocalDate end;
	private boolean isApproved;
		
	public HolidayClaimDto() {	}
	
	public HolidayClaimDto(String claimant, LocalDate start, LocalDate end) {
		this.claimant = claimant;
		this.start = start;
		this.end = end;
		this.timeOfApplication = LocalDate.now();
		this.isApproved = false;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
