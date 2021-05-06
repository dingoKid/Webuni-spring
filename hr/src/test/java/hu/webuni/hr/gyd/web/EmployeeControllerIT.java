package hu.webuni.hr.gyd.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.gyd.dto.EmployeeDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {
	
	private static final String BASE_URI = "/api/employees";
	
	@Autowired
	WebTestClient webTestClient;

	@Test
	void testThatEmployeeIsAdded() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();	
		EmployeeDto newEmployee = new EmployeeDto(5L, "kiss imre", "Driver", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));
		
		addEmployee(newEmployee);
		
		List<EmployeeDto> employeesAfter = getAllEmployees();
		employeesAfter.removeAll(employeesBefore);
		assertThat(employeesAfter).containsExactly(newEmployee);
	}
	
	@Test
	void testThatEmployeeValidationFailsNoPositionSpecifiedWhenAdding() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		EmployeeDto newEmployee = new EmployeeDto(5L, "kiss imre", "", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));

		addEmployeeExpectedBadRequest(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesBefore).hasSameElementsAs(employeesAfter);
		employeesAfter.removeAll(employeesBefore);
		assertThat(employeesAfter).size().isEqualTo(0);
	}
	
	@Test
	void testThatEmployeeValidationFailsNoNameSpecifiedWhenAdding() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		EmployeeDto newEmployee = new EmployeeDto(5L, "", "driver", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));

		addEmployeeExpectedBadRequest(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();
	
		assertThat(employeesBefore).hasSameElementsAs(employeesAfter);
		employeesAfter.removeAll(employeesBefore);
		assertThat(employeesAfter).size().isEqualTo(0);
	}

	@Test
	void testThatEmployeeValidationFailsFutureDateSpecifiedWhenAdding() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		EmployeeDto newEmployee = new EmployeeDto(15L, "kiss imre", "driver", 150000, LocalDateTime.now().plusHours(1));

		addEmployeeExpectedBadRequest(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();
		
		assertThat(employeesBefore).hasSameElementsAs(employeesAfter);
		employeesAfter.removeAll(employeesBefore);		
		assertThat(employeesAfter).size().isEqualTo(0);
	}
	
	@Test
	void testThatEmployeeIsModified() throws Exception {
		EmployeeDto employeeBefore = getById(1L);
		EmployeeDto modifierEmployee = new EmployeeDto(1L, "kiss imre", "Driver", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));
		modifyEmployee(employeeBefore.getEmployeeId(), modifierEmployee);
		EmployeeDto newEmployee = getById(1L);
		assertThat(newEmployee).usingRecursiveComparison().isEqualTo(modifierEmployee);
	}
	
	@Test
	void testThatEmployeeIdNotExistInDatabaseWhenModifying() throws Exception {
		EmployeeDto employeeBefore = getByIdExpectedNotFound(35L);

		assertThat(employeeBefore).isEqualTo(new EmployeeDto(null, null, null, 0, null));
	}
	
	@Test
	void testThatEmployeeValidationFailsNoNameSpecifiedWhenModifying() throws Exception {
		EmployeeDto employeeBefore = getById(1L);
		EmployeeDto modifierEmployee = new EmployeeDto(1L, "", "worker", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));
		
		modifyEmployeeExpectedBadRequest(employeeBefore.getEmployeeId(), modifierEmployee);
		EmployeeDto newEmployee = getById(1L);
		
		assertThat(newEmployee).usingRecursiveComparison().isNotEqualTo(modifierEmployee);
		assertThat(newEmployee).usingRecursiveComparison().isEqualTo(employeeBefore);
	}
	
	@Test
	void testThatEmployeeValidationFailsNoPositionSpecifiedWhenModifying() throws Exception {
		EmployeeDto employeeBefore = getById(1L);
		EmployeeDto modifierEmployee = new EmployeeDto(1L, "kiss imre", "", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));
		
		modifyEmployeeExpectedBadRequest(employeeBefore.getEmployeeId(), modifierEmployee);
		EmployeeDto newEmployee = getById(1L);
		
		assertThat(newEmployee).usingRecursiveComparison().isNotEqualTo(modifierEmployee);
		assertThat(newEmployee).usingRecursiveComparison().isEqualTo(employeeBefore);
	}
	
	@Test
	void testThatEmployeeValidationFailsZeroSalaryAddedWhenModifying() throws Exception {
		EmployeeDto employeeBefore = getById(1L);
		EmployeeDto modifierEmployee = new EmployeeDto(1L, "kiss imre", "driver", 0, LocalDateTime.of(2015, 11, 10, 0, 0));
		
		modifyEmployeeExpectedBadRequest(employeeBefore.getEmployeeId(), modifierEmployee);
		EmployeeDto newEmployee = getById(1L);
		
		assertThat(newEmployee).usingRecursiveComparison().isNotEqualTo(modifierEmployee);
		assertThat(newEmployee).usingRecursiveComparison().isEqualTo(employeeBefore);
	}
	
	@Test
	void testThatEmployeeValidationFailsNegativeSalaryAddedWhenModifying() throws Exception {
		EmployeeDto employeeBefore = getById(1L);
		EmployeeDto modifierEmployee = new EmployeeDto(1L, "kiss imre", "driver", -10000, LocalDateTime.of(2015, 11, 10, 0, 0));
		
		modifyEmployeeExpectedBadRequest(employeeBefore.getEmployeeId(), modifierEmployee);
		EmployeeDto newEmployee = getById(1L);
		
		assertThat(newEmployee).usingRecursiveComparison().isNotEqualTo(modifierEmployee);
		assertThat(newEmployee).usingRecursiveComparison().isEqualTo(employeeBefore);
	}
	
	private void modifyEmployee(long employeeId, EmployeeDto modifierEmployee) {
		webTestClient
			.put()
			.uri(BASE_URI + "/" + employeeId)
			.bodyValue(modifierEmployee)
			.exchange()
			.expectStatus()
			.isOk();
		
	}
	
	private void modifyEmployeeExpectedBadRequest(long employeeId, EmployeeDto modifierEmployee) {
		webTestClient
			.put()
			.uri(BASE_URI + "/" + employeeId)
			.bodyValue(modifierEmployee)
			.exchange()
			.expectStatus()
			.isBadRequest();
		
	}

	private EmployeeDto getById(long employeeId) {
		return webTestClient
			.get()
			.uri(BASE_URI + "/" + employeeId)
			.exchange()
			.expectStatus().isOk()
			.expectBody(EmployeeDto.class)
			.returnResult().getResponseBody();
	}
	
	private EmployeeDto getByIdExpectedNotFound(long employeeId) {
		return webTestClient
			.get()
			.uri(BASE_URI + "/" + employeeId)
			.exchange()
			.expectStatus().isEqualTo(404)
			.expectBody(EmployeeDto.class)
			.returnResult().getResponseBody();
	}

	private void addEmployeeExpectedBadRequest(EmployeeDto newEmployee) {
		webTestClient
			.post()
			.uri(BASE_URI)
			.bodyValue(newEmployee)
			.exchange()
			.expectStatus()
			//.isEqualTo(400)
			.isBadRequest();
	}

	private void addEmployee(EmployeeDto newEmployee) {
		webTestClient
			.post()
			.uri(BASE_URI)
			.bodyValue(newEmployee)
			.exchange()
			.expectStatus()
			.isOk();
	}

	private List<EmployeeDto> getAllEmployees() {
		List<EmployeeDto> allEmployees = webTestClient
			.get()
			.uri(BASE_URI)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(EmployeeDto.class)
			.returnResult().getResponseBody();
		
		return allEmployees;
	}
	
	
}
