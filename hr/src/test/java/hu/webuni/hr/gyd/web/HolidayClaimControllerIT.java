package hu.webuni.hr.gyd.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

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
