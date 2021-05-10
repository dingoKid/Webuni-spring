package hu.webuni.hr.gyd.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.repository.HolidayClaimRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HolidayClaimControllerIT {

private static final String BASE_URI = "/api/holidays";
	
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	HolidayClaimRepository claimRepository;
	
	@Test
	void testThatClaimIsAdded() throws Exception {
		List<HolidayClaimDto> claimsBefore = getAllClaims();
		Long employeeId = 1L;
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 1, 10), LocalDate.of(2020, 3, 5));
		newClaim = createClaim(newClaim, employeeId);
		List<HolidayClaimDto> claimsAfter = getAllClaims();

		claimsAfter.removeAll(claimsBefore);
		assertThat(claimsAfter.get(0)).usingRecursiveComparison().isEqualTo(newClaim);		
	}
	
	@Test
	void testThatClaimIsNotAddedBecauseEmployeeDoesNotExist() throws Exception {
		List<HolidayClaimDto> claimsBefore = getAllClaims();
		Long employeeId = 100L;
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 1, 10), LocalDate.of(2020, 3, 5));
		newClaim = createClaimExpectedNotFound(newClaim, employeeId);
		List<HolidayClaimDto> claimsAfter = getAllClaims();

		assertThat(claimsBefore).hasSameElementsAs(claimsAfter);
		claimsAfter.removeAll(claimsBefore);
		assertThat(claimsAfter.size()).isEqualTo(0);		
	}
	
	@Test
	void testThatClaimIsApproved() throws Exception {
		Long employeeId = 1L;
		Long principalId = 5L;
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 1, 10), LocalDate.of(2020, 3, 5));
		newClaim = createClaim(newClaim, employeeId);
		
		HolidayClaimDto approvedClaim = approveClaim(newClaim.getClaimNumber(), principalId);
		
		assertThat(newClaim).usingRecursiveComparison().ignoringFields("isApproved", "principal").isEqualTo(approvedClaim);
		assertThat(newClaim).usingRecursiveComparison().ignoringFields("principal").isNotEqualTo(approvedClaim);
		assertThat(newClaim).usingRecursiveComparison().ignoringFields("isApproved").isNotEqualTo(approvedClaim);
		assertThat(newClaim.isApproved()).isNotEqualTo(approvedClaim.isApproved());
	}
	
	@Test
	void testThatClaimIsNotApprovedBecauseClaimDoesNotExist() throws Exception {
		Long claimId = 100L;
		Long principalId = 5L;
		
		HolidayClaimDto claim = approveClaimExpectedNotFound(claimId, principalId);
		
		assertThat(claim.isApproved()).isNull();
	}
	
	@Test
	void testThatClaimIsNotApprovedBecausePrincipalDoesNotExist() throws Exception {
		Long claimId = 1L;
		Long principalId = 105L;
		
		HolidayClaimDto claim = approveClaimExpectedNotFound(claimId, principalId);
		
		assertThat(claim.isApproved()).isNull();
	}
	
	@Test
	void testThatClaimIsModified() throws Exception {
		Long employeeId = 1L;
		HolidayClaimDto oldClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 2, 10), LocalDate.of(2020, 3, 5));
		oldClaim = createClaim(oldClaim, employeeId);
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 2, 22), LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 10));
		
		oldClaim = modifyClaim(oldClaim.getClaimNumber(), newClaim);
		
		assertThat(oldClaim).usingRecursiveComparison().ignoringFields("claimNumber", "claimant").isEqualTo(newClaim);
	}
	
	@Test
	void testThatClaimIsNotModifiedBecauseClaimDoesNotExist() throws Exception {
		Long claimId = 110L;
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 2, 22), LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 10));
		
		HolidayClaimDto claim = modifyClaimExpectedNotFound(claimId, newClaim);
		
		assertThat(claim).usingRecursiveComparison().ignoringFields("claimNumber", "claimant").isNotEqualTo(newClaim);
		assertThat(claim).usingRecursiveComparison().isEqualTo(new HolidayClaimDto());
	}
	
	@Test
	void testThatClaimIsDeleted() throws Exception {
		List<HolidayClaimDto> claimsBefore = getAllClaims();
		HolidayClaimDto claim = claimsBefore.get(0);
		
		deleteClaim(claim.getClaimNumber());
		List<HolidayClaimDto> claimsAfter= getAllClaims();
		
		assertThat(claim).isNull();
	}

	private void deleteClaim(Long claimNumber) {
		webTestClient
			.delete()
			.uri(BASE_URI + "/delete/" + claimNumber)
			.exchange()
			.expectStatus().isOk();			
	}

	private HolidayClaimDto modifyClaimExpectedNotFound(Long claimNumber, HolidayClaimDto newClaim) {
		return webTestClient
				.put()
				.uri(BASE_URI + "/modify/" + claimNumber)
				.bodyValue(newClaim)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayClaimDto modifyClaim(Long claimNumber, HolidayClaimDto newClaim) {
		return webTestClient
				.put()
				.uri(BASE_URI + "/modify/" + claimNumber)
				.bodyValue(newClaim)
				.exchange()
				.expectStatus().isOk()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayClaimDto approveClaim(Long claimNumber, Long principalId) {
		return webTestClient
				.get()
				.uri(BASE_URI + "/approve/" + claimNumber + "/" + principalId)
				.exchange()
				.expectStatus().isOk()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private HolidayClaimDto approveClaimExpectedNotFound(Long claimNumber, Long principalId) {
		return webTestClient
				.get()
				.uri(BASE_URI + "/approve/" + claimNumber + "/" + principalId)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayClaimDto createClaim(HolidayClaimDto newClaim, Long employeeId) {
		return webTestClient
			.post()
			.uri(BASE_URI + "/" + employeeId)
			.bodyValue(newClaim)
			.exchange()
			.expectStatus().isOk()
			.expectBody(HolidayClaimDto.class)
			.returnResult()
			.getResponseBody();
	}
	
	private HolidayClaimDto createClaimExpectedNotFound(HolidayClaimDto newClaim, Long employeeId) {
		return webTestClient
			.post()
			.uri(BASE_URI + "/" + employeeId)
			.bodyValue(newClaim)
			.exchange()
			.expectStatus().isNotFound()
			.expectBody(HolidayClaimDto.class)
			.returnResult()
			.getResponseBody();
	}

	private List<HolidayClaimDto> getAllClaims() {
		return webTestClient
				.get()
				.uri(BASE_URI)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}
	

}
