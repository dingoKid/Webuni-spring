package hu.webuni.hr.gyd.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HrUser;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.HolidayClaimRepository;
import hu.webuni.hr.gyd.repository.HrUserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HolidayClaimControllerIT {

private static final String BASE_URI = "/api/holidays";
	
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	HolidayClaimRepository claimRepository;
	
	@Autowired
	HrUserRepository userRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
    private TestRestTemplate template;
	
	@BeforeEach
	private void clearUsers() {
		userRepository.deleteAll();
	}
	
	private void createTestUser(String username, String password) {
		
		HrUser hu1 = new HrUser(username, passwordEncoder.encode(password), null);
		hu1 = userRepository.save(hu1);	
		
		Employee employee = new Employee();
		employee.setUsername(username);
		employeeRepository.save(employee);
	}
	
//	@Test
//	void testThatClaimIsAddedWithAuth() throws Exception {	
//		String username = "user1";
//		String password = "pass";
//		createTestUser(username, password);
//		List<HolidayClaimDto> claimsBefore = getAllClaimsWithAuth(username, password);
//		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 10), LocalDate.of(2020, 3, 5));
//
//		newClaim = createClaimWithAuth(newClaim, username, password);
//		List<HolidayClaimDto> claimsAfter = getAllClaimsWithAuth(username, password);
//
//		claimsAfter.removeAll(claimsBefore);
//		assertThat(claimsAfter.get(0)).usingRecursiveComparison().isEqualTo(newClaim);		
//	}
//	
//	
//	private List<HolidayClaimDto> getAllClaimsWithAuth(String username, String password) {
//		
//		HolidayClaimDto[] claims = template.withBasicAuth(username, password)
//				.getForObject(BASE_URI, HolidayClaimDto[].class);
//		List<HolidayClaimDto> result = new ArrayList<>();
//		Collections.addAll(result, claims);
//		return result;
//	}
//
//	private HolidayClaimDto createClaimWithAuth(HolidayClaimDto newClaim, String username, String password) {
//		return template.withBasicAuth(username, password)
//				.postForObject(BASE_URI, newClaim, HolidayClaimDto.class);
//	}


	
	@Test
	void testThatClaimIsAdded() throws Exception {
		String username = "user1";
		String pass = "pass";
		createTestUser(username, pass);
		
		List<HolidayClaimDto> claimsBefore = getAllClaims(username, pass);
		Long employeeId = 1L;
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 1, 10), LocalDate.of(2020, 3, 5));
		newClaim = createClaim(newClaim, employeeId, username, pass);
		List<HolidayClaimDto> claimsAfter = getAllClaims(username, pass);

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
		Long claimId = 2L;
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
	void testThatClaimIsNotModifiedIfApproved() throws Exception {
		Long employeeId = 1L;
		HolidayClaimDto oldClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 2, 10), LocalDate.of(2020, 3, 5));
		oldClaim = createClaim(oldClaim, employeeId);
		oldClaim = approveClaim(oldClaim.getClaimNumber(), 2L);
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 2, 22), LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 10));
		
		HolidayClaimDto modifiedClaim = modifyClaim(oldClaim.getClaimNumber(), newClaim);
		
		assertThat(oldClaim).usingRecursiveComparison().isEqualTo(modifiedClaim);
	}
	
	@Test
	void testThatClaimIsDeletedIfNotApproved() throws Exception {
		HolidayClaimDto claim = new HolidayClaimDto(LocalDate.of(2020, 2, 22), LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 10));
		claim = createClaim(claim, 1L);
		claim.setPrincipal(null);
		claim = modifyClaim(claim.getClaimNumber(), claim);
		List<HolidayClaimDto> claimsBefore = getAllClaims();
		assertThat(claimsBefore).contains(claim);
		
		deleteClaim(claim.getClaimNumber());
		
		List<HolidayClaimDto> claimsAfter = getAllClaims();
		assertThat(claimsAfter).doesNotContain(claim);
	}
	
	@Test
	void testThatClaimIsNotDeletedIfApproved() throws Exception {
		HolidayClaimDto claim = new HolidayClaimDto(LocalDate.of(2020, 2, 22), LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 10));
		claim = createClaim(claim, 1L);
		claim = approveClaim(claim.getClaimNumber(), 2L);		
		
		List<HolidayClaimDto> claimsBefore = getAllClaims();
		
		deleteClaim(claim.getClaimNumber());
		List<HolidayClaimDto> claimsAfter = getAllClaims();
		
		assertThat(claimsBefore).hasSameElementsAs(claimsAfter);
	}
	
	@Test
	void testSearchForApproval() throws Exception {
		String paramName = "approved";
		Boolean paramValue = true;
		List<HolidayClaimDto> searchResultsForIsApprovedEqualsTrue = getAllClaims().stream()
				.filter(c -> c.isApproved() == paramValue).collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForIsApproved(paramName, paramValue);
		
		assertThat(searchResultsForIsApprovedEqualsTrue).hasSameElementsAs(queryResults);		
	}
	
	@Test
	void testSearchForClaimantNamePartlyMatched() throws Exception {
		String paramName = "claimant";
		String paramValue = "Magy";
		List<HolidayClaimDto> searchResultsForClaimantName = getAllClaims().stream()
				.filter(c -> c.getClaimant().startsWith(paramValue)).collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForClaimant(paramName, paramValue);
		
		assertThat(searchResultsForClaimantName).hasSameElementsAs(queryResults);		
	}
	
	@Test
	void testSearchForClaimantNameIgnoringCase() throws Exception {
		String paramName = "claimant";
		String paramValue = "mAgYaR";
		String searchString = "Magyar";
		List<HolidayClaimDto> searchResultsForClaimantName = getAllClaims().stream()
				.filter(c -> c.getClaimant().startsWith(searchString))
				.collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForClaimant(paramName, paramValue);
		
		assertThat(searchResultsForClaimantName).hasSameElementsAs(queryResults);		
	}
	
	@Test
	void testSearchForApplicationDate() throws Exception {
		String param1Name = "applicationStart";
		String param2Name = "applicationEnd";
		String param1Value = "2020-02-10";
		String param2Value = "2020-03-26";
		List<HolidayClaimDto> searchResultsForApplicationDate = getAllClaims().stream()
				.filter(c -> c.getTimeOfApplication().isAfter(LocalDate.of(2020, 2, 10)) && c.getTimeOfApplication().isBefore(LocalDate.of(2020, 3, 26)))
				.collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForDates(param1Name, param1Value, param2Name, param2Value);
		assertThat(searchResultsForApplicationDate).hasSameElementsAs(queryResults);
	}
	
	@Test
	void testSearchForHolidayDate() throws Exception {
		String param1Name = "holidayStart";
		String param2Name = "holidayEnd";
		String param1Value = "2020-02-10";
		String param2Value = "2020-04-10";
		List<HolidayClaimDto> searchResultsForApplicationDate = getAllClaims().stream()
				.filter(c -> c.getStart().isBefore(LocalDate.of(2020, 4, 10)) || c.getStart().isEqual(LocalDate.of(2020, 4, 10)) 
						&& LocalDate.of(2020, 2, 10).isBefore(c.getEnding()) || LocalDate.of(2020, 2, 10).isEqual(c.getEnding()))
				.collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForDates(param1Name, param1Value, param2Name, param2Value);
		assertThat(searchResultsForApplicationDate).hasSameElementsAs(queryResults);
	}

	private List<HolidayClaimDto> searchForDates(String param1Name, String param1Value, String param2Name,
			String param2Value) {
		return webTestClient
				.get()
				.uri(uriBuilder ->
					uriBuilder
					.path(BASE_URI + "/search")
					.queryParam(param1Name, param1Value)
					.queryParam(param2Name, param2Value)
					.build())
				.headers(headers -> headers.setBasicAuth("kiss1", "pass"))
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private List<HolidayClaimDto> searchForClaimant(String paramName, String paramValue) {
		return webTestClient
				.get()
				.uri(uriBuilder ->
						uriBuilder
						.path(BASE_URI + "/search")
						.queryParam(paramName, paramValue)
						.build())
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private List<HolidayClaimDto> searchForIsApproved(String paramName, Boolean paramValue) {
		return webTestClient
				.get()
				.uri(uriBuilder ->
						uriBuilder
						.path(BASE_URI + "/search")
						.queryParam(paramName, paramValue)
						.build())
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
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
	
	private HolidayClaimDto createClaim(HolidayClaimDto newClaim, Long employeeId, String username, String pass) {
		return webTestClient
				.post()
				.uri(BASE_URI + "/" + employeeId)
				.headers(headers -> headers.setBasicAuth(username, pass))
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
	
	private List<HolidayClaimDto> getAllClaims(String username, String pass) {
		return webTestClient
				.get()
				.uri(BASE_URI)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}


}
