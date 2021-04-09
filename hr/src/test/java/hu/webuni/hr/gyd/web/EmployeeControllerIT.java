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
		
		EmployeeDto newEmployee = new EmployeeDto(5L, "kiss imre", "driver", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));
		addEmployee(newEmployee);
		
		List<EmployeeDto> employeesAfter = getAllEmployees();
		employeesAfter.removeAll(employeesBefore);
		
		assertThat(employeesAfter).containsExactly(newEmployee);
	}
	
	@Test
	void testThatEmployeeValidationFailsNoPositionSpecified() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		
		EmployeeDto newEmployee = new EmployeeDto(5L, "kiss imre", "", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));
		addEmployeeExpectedBadRequest(newEmployee);
		
		List<EmployeeDto> employeesAfter = getAllEmployees();
		employeesAfter.removeAll(employeesBefore);
		
		assertThat(employeesAfter).size().isEqualTo(0);
	}
	
	@Test
	void testThatEmployeeValidationFailsNoNameSpecified() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();
		
		EmployeeDto newEmployee = new EmployeeDto(5L, "", "driver", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));
		addEmployeeExpectedBadRequest(newEmployee);
		
		List<EmployeeDto> employeesAfter = getAllEmployees();
		employeesAfter.removeAll(employeesBefore);
		
		assertThat(employeesAfter).size().isEqualTo(0);
	}
	
	@Test
	void testThatEmployeeIsModified() throws Exception {
		EmployeeDto employeeBefore = getById(1L);
		
		/*EmployeeDto modifiedEmployee = new EmployeeDto(employeeBefore.getEmployeeId(), employeeBefore.getName(),
										employeeBefore.getPosition(), employeeBefore.getSalary(), employeeBefore.getHiringDate());*/
		EmployeeDto modifierEmployee = new EmployeeDto(1L, "kiss imre", "", 150000, LocalDateTime.of(2015, 11, 10, 0, 0));
		
		modifyEmployee(employeeBefore.getEmployeeId(), modifierEmployee);
		
		EmployeeDto newEmployee = getById(1L);
		
		assertThat(newEmployee).usingRecursiveComparison().isEqualTo(modifierEmployee);
		
		
		
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

	private EmployeeDto getById(long employeeId) {
		return webTestClient
			.get()
			.uri(BASE_URI + "/" + employeeId)
			.exchange()
			.expectStatus().isOk()
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
