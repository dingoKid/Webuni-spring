package hu.webuni.hr.gyd.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.gyd.dto.HolidayClaimDto;
import hu.webuni.hr.gyd.dto.HolidayClaimSearchDto;
import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HrUser;
import hu.webuni.hr.gyd.model.HrUserDetails;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.HolidayClaimRepository;
import hu.webuni.hr.gyd.repository.HrUserRepository;
import hu.webuni.hr.gyd.security.JwtService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
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
	JwtService jwtService;
	
	private final String username = "user1";
	private final String pass = "pass";
	
	@BeforeEach
	private void clearUsers() {
		createTestUser(username, pass);
	}
	
	private void createTestUser(String username, String password) {		
		
		if(!userRepository.existsById(username)) {
			HrUser hu1 = new HrUser(username, passwordEncoder.encode(password), null);
			hu1 = userRepository.save(hu1);			
			Employee employee = new Employee();
			employee.setUsername(username);
			employeeRepository.save(employee);
		}
		
	}
	
	private void createPrincipalForUser(String username, String principalUsername, String password) {
		
		if(employeeRepository.findByUsername(principalUsername).isEmpty()) {
			HrUser hu1 = new HrUser(principalUsername, passwordEncoder.encode(password), null);
			hu1 = userRepository.save(hu1);
	
			Employee principal = new Employee();
			principal.setUsername(principalUsername);
			principal.setName("Kovacs");
			principal = employeeRepository.save(principal);
			
			Employee user = employeeRepository.findByUsername(username).get();
			user.setPrincipal(principal);
			employeeRepository.save(user);
		}
		
	}
	
	@Test
	void testThatClaimIsAdded() throws Exception {
		
		String token = jwtService.createJwtToken(new HrUserDetails("user1", "pass", Set.of("admin").stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList())));
		
		List<HolidayClaimDto> claimsBefore = getAllClaims(token);
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 1, 10), LocalDate.of(2020, 3, 5));
		newClaim = createClaim(newClaim, token);
		List<HolidayClaimDto> claimsAfter = getAllClaims(token);
		/*List<HolidayClaimDto> claimsBefore = getAllClaims(username, pass);
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 1, 10), LocalDate.of(2020, 3, 5));
		newClaim = createClaim(newClaim, username, pass);
		List<HolidayClaimDto> claimsAfter = getAllClaims(username, pass);*/

		claimsAfter.removeAll(claimsBefore);
		assertThat(claimsAfter.get(0)).usingRecursiveComparison().isEqualTo(newClaim);		
	}
	
	
	@Test
	void testThatClaimIsApproved() throws Exception {
		String principalUsername = "principal";
		String principalPass = "pass";
		createPrincipalForUser(username, principalUsername, principalPass);
		
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 1, 10), LocalDate.of(2020, 3, 5));
		newClaim = createClaim(newClaim, username, pass);
		
		HolidayClaimDto approvedClaim = approveClaim(newClaim.getClaimNumber(), principalUsername, principalPass);
		assertThat(newClaim).usingRecursiveComparison().ignoringFields("isApproved", "principal").isEqualTo(approvedClaim);
		assertThat(newClaim).usingRecursiveComparison().ignoringFields("principal").isNotEqualTo(approvedClaim);
		assertThat(newClaim).usingRecursiveComparison().ignoringFields("isApproved").isNotEqualTo(approvedClaim);
		assertThat(newClaim.getIsApproved()).isNotEqualTo(approvedClaim.getIsApproved());
	}
	
	@Test
	void testThatClaimIsNotApprovedBecauseClaimDoesNotExist() throws Exception {
		Long claimId = 100L;
		
		HolidayClaimDto claim = approveClaimExpectedNotFound(claimId, username, pass);
		
		assertThat(claim.getIsApproved()).isNull();
	}
	
	@Test
	void testThatClaimIsNotApprovedBecauseUserIsNotPrincipal() throws Exception {
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 1, 10), LocalDate.of(2020, 3, 5));
		newClaim = createClaim(newClaim, username, pass);
		
		String otherUsername = "other";
		String otherPass = "other";
		HrUser other = new HrUser(otherUsername, otherPass, null);
		userRepository.save(other);
		Employee emp = new Employee();
		emp.setUsername(otherUsername);
		employeeRepository.save(emp);
		
		HolidayClaimDto claim = approveClaimExpectedForbidden(newClaim.getClaimNumber(), username, pass);
		
		assertThat(claim.getIsApproved()).isNull();
	}
	
	@Test
	void testThatClaimIsModified() throws Exception {
		HolidayClaimDto oldClaim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 2, 10), LocalDate.of(2020, 3, 5));
		oldClaim = createClaim(oldClaim, username, pass);
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 2, 22), LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 10));
		
		oldClaim = modifyClaim(oldClaim.getClaimNumber(), newClaim, username, pass);
		
		assertThat(oldClaim.getStart()).isEqualTo(newClaim.getStart());
		assertThat(oldClaim.getEnding()).isEqualTo(newClaim.getEnding());
		assertThat(oldClaim.getTimeOfApplication()).isEqualTo(newClaim.getTimeOfApplication());
	}
	
	@Test
	void testThatClaimIsDeletedIfNotApproved() throws Exception {
		HolidayClaimDto claim = new HolidayClaimDto(LocalDate.of(2020, 2, 22), LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 10));
		claim = createClaim(claim, username, pass);
		claim.setPrincipal(null);
		claim = modifyClaim(claim.getClaimNumber(), claim, username, pass);
		
		List<HolidayClaimDto> claimsBefore = getAllClaims(username, pass);
		assertThat(claimsBefore).contains(claim);
		
		deleteClaim(claim.getClaimNumber(), username, pass);
		
		List<HolidayClaimDto> claimsAfter = getAllClaims(username, pass);
		assertThat(claimsAfter).doesNotContain(claim);
	}
	
	@Test
	void testThatClaimIsNotModifiedBecauseClaimDoesNotExist() throws Exception {
		Long claimId = 110L;
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 2, 22), LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 10));
		
		HolidayClaimDto claim = modifyClaimExpectedNotFound(claimId, newClaim, username, pass);
		
		assertThat(claim).usingRecursiveComparison().ignoringFields("claimNumber", "claimant").isNotEqualTo(newClaim);
		assertThat(claim).usingRecursiveComparison().isEqualTo(new HolidayClaimDto());
	}
	
	@Test
	void testThatClaimIsNotModifiedIfApproved() throws Exception {
		HolidayClaimDto claimBefore = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 2, 10), LocalDate.of(2020, 3, 5));
		claimBefore = createClaim(claimBefore, username, pass);
		
		String principalUsername = "principal";
		String principalPass = "pass";
		createPrincipalForUser(username, principalUsername, principalPass);		
		claimBefore = approveClaim(claimBefore.getClaimNumber(), principalUsername, principalPass);
		
		HolidayClaimDto newClaim = new HolidayClaimDto(LocalDate.of(2020, 2, 22), LocalDate.of(2020, 2, 25), LocalDate.of(2020, 3, 10));
		
		claimBefore = modifyClaimExpectedBadRequest(claimBefore.getClaimNumber(), newClaim, username, pass);
		
		
		assertThat(claimBefore.getStart()).isNotEqualTo(newClaim.getStart());
		assertThat(claimBefore.getEnding()).isNotEqualTo(newClaim.getEnding());
		assertThat(claimBefore.getTimeOfApplication()).isNotEqualTo(newClaim.getTimeOfApplication());
	}
	
	
	@Test
	void testThatClaimIsNotDeletedIfApproved() throws Exception {
		HolidayClaimDto claim = new HolidayClaimDto(LocalDate.of(2020, 1, 20), LocalDate.of(2020, 2, 10), LocalDate.of(2020, 3, 5));
		claim = createClaim(claim, username, pass);
		
		String principalUsername = "principal";
		String principalPass = "pass";
		createPrincipalForUser(username, principalUsername, principalPass);
		claim = approveClaim(claim.getClaimNumber(), principalUsername, principalPass);
		
		List<HolidayClaimDto> claimsBefore = getAllClaims(username, pass);		
		deleteClaimExpectedBadRequest(claim.getClaimNumber(), username, pass);
		List<HolidayClaimDto> claimsAfter = getAllClaims(username, pass);
		
		assertThat(claimsBefore).hasSameElementsAs(claimsAfter);
	}
	
	@Test
	@Order(3)
	void testSearchForApproval() throws Exception {
		HolidayClaimSearchDto search = new HolidayClaimSearchDto();
		search.setIsApproved(true);
		
		List<HolidayClaimDto> searchResultsForIsApprovedEqualsTrue = getAllClaims(username, pass).stream()
				.filter(c -> c.getIsApproved() != null && c.getIsApproved() == true).collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForIsApproved(search, username, pass);
		
		assertThat(searchResultsForIsApprovedEqualsTrue).hasSameElementsAs(queryResults);		
	}
	
	@Test
	@Order(1)
	void testSearchForClaimantNamePartlyMatched() throws Exception {
		HolidayClaimSearchDto search = new HolidayClaimSearchDto();
		String value = "Magy";
		search.setClaimant(value);
		
		List<HolidayClaimDto> searchResultsForClaimantName = getAllClaims(username, pass).stream()
				.filter(c -> c.getClaimant().startsWith(value))
				.collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForClaimant(search, username, pass);
		
		assertThat(searchResultsForClaimantName).hasSameElementsAs(queryResults);
	}
	
	@Test
	@Order(2)
	void testSearchForClaimantNameIgnoringCase() throws Exception {
		HolidayClaimSearchDto search = new HolidayClaimSearchDto();
		String value1 = "mAgYaR";
		search.setClaimant(value1);
		
		String value2 = "Magyar";
		List<HolidayClaimDto> searchResultsForClaimantName = getAllClaims(username, pass).stream()
				.filter(c -> c.getClaimant().startsWith(value2))
				.collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForClaimant(search, username, pass);
		
		assertThat(searchResultsForClaimantName).hasSameElementsAs(queryResults);
	}
	
	@Test
	@Order(4)
	void testSearchForApplicationDate() throws Exception {
		HolidayClaimSearchDto search = new HolidayClaimSearchDto();
		LocalDate startDate = LocalDate.of(2020, 2, 10);
		LocalDate endDate = LocalDate.of(2020, 3, 26);
		search.setStartOfApplication(startDate);
		search.setEndOfApplication(endDate);
		
		List<HolidayClaimDto> searchResultsForApplicationDate = getAllClaims(username, pass).stream()
				.filter(c -> c.getTimeOfApplication().isAfter(startDate) && c.getTimeOfApplication().isBefore(endDate))
				.collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForDates(search, username, pass);
		assertThat(searchResultsForApplicationDate).hasSameElementsAs(queryResults);
	}
	
	@Test
	@Order(5)
	void testSearchForHolidayDate() throws Exception {
		HolidayClaimSearchDto search = new HolidayClaimSearchDto();
		LocalDate startDate = LocalDate.of(2020, 2, 10);
		LocalDate endDate = LocalDate.of(2020, 4, 10);
		search.setStart(startDate);
		search.setEnding(endDate);
		
		List<HolidayClaimDto> searchResultsForDateOfHoliday = getAllClaims(username, pass).stream()
				.filter(c -> c.getStart().isBefore(endDate) || c.getStart().isEqual(endDate) 
						&& startDate.isBefore(c.getEnding()) || startDate.isEqual(c.getEnding()))
				.collect(Collectors.toList());
		
		List<HolidayClaimDto> queryResults = searchForDates(search, username, pass);
		assertThat(searchResultsForDateOfHoliday).hasSameElementsAs(queryResults);
	}

	private List<HolidayClaimDto> searchForDates(HolidayClaimSearchDto search, String username, String pass) {
		return webTestClient
				.post()
				.uri(BASE_URI + "/search")
				.bodyValue(search)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private List<HolidayClaimDto> searchForClaimant(HolidayClaimSearchDto search, String username, String pass) {
		return webTestClient
				.post()
				.uri(BASE_URI + "/search")
				.bodyValue(search)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private List<HolidayClaimDto> searchForIsApproved(HolidayClaimSearchDto search, String username, String pass) {
		return webTestClient
				.post()
				.uri(BASE_URI + "/search")
				.bodyValue(search)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private void deleteClaim(Long claimNumber, String username, String pass) {
		webTestClient
			.delete()
			.uri(BASE_URI + "/delete/" + claimNumber)
			.headers(headers -> headers.setBasicAuth(username, pass))
			.exchange()
			.expectStatus().isOk();			
	}
	
	private void deleteClaimExpectedBadRequest(Long claimNumber, String username, String pass) {
		webTestClient
		.delete()
		.uri(BASE_URI + "/delete/" + claimNumber)
		.headers(headers -> headers.setBasicAuth(username, pass))
		.exchange()
		.expectStatus().isBadRequest();			
	}

	private HolidayClaimDto modifyClaimExpectedNotFound(Long claimNumber, HolidayClaimDto newClaim, String username, String pass) {
		return webTestClient
				.put()
				.uri(BASE_URI + "/modify/" + claimNumber)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.bodyValue(newClaim)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayClaimDto modifyClaim(Long claimNumber, HolidayClaimDto newClaim, String username, String pass) {
		return webTestClient
				.put()
				.uri(BASE_URI + "/modify/" + claimNumber)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.bodyValue(newClaim)
				.exchange()
				.expectStatus().isOk()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private HolidayClaimDto modifyClaimExpectedBadRequest(Long claimNumber, HolidayClaimDto newClaim, String username, String pass) {
		return webTestClient
				.put()
				.uri(BASE_URI + "/modify/" + claimNumber)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.bodyValue(newClaim)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayClaimDto approveClaim(Long claimNumber, String username, String pass) {
		return webTestClient
				.get()
				.uri(BASE_URI + "/approve/" + claimNumber)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.exchange()
				.expectStatus().isOk()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private HolidayClaimDto approveClaimExpectedForbidden(Long claimNumber, String username, String pass) {
		return webTestClient
				.get()
				.uri(BASE_URI + "/approve/" + claimNumber)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.exchange()
				.expectStatus().isForbidden()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private HolidayClaimDto approveClaimExpectedNotFound(Long claimNumber, String username, String pass) {
		return webTestClient
				.get()
				.uri(BASE_URI + "/approve/" + claimNumber)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

	private HolidayClaimDto createClaim(HolidayClaimDto newClaim, String username, String pass) {
		return webTestClient
				.post()
				.uri(BASE_URI)
				.headers(headers -> headers.setBasicAuth(username, pass))
				.bodyValue(newClaim)
				.exchange()
				.expectStatus().isOk()
				.expectBody(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private HolidayClaimDto createClaim(HolidayClaimDto newClaim, String token) {
		return webTestClient
				.post()
				.uri(BASE_URI)
				.headers(headers -> headers.setBearerAuth(token))
				.bodyValue(newClaim)
				.exchange()
				.expectStatus().isOk()
				.expectBody(HolidayClaimDto.class)
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
	
	private List<HolidayClaimDto> getAllClaims(String token) {
		return webTestClient
				.get()
				.uri(BASE_URI)
				.headers(headers -> headers.setBearerAuth(token))
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(HolidayClaimDto.class)
				.returnResult()
				.getResponseBody();
	}

}
