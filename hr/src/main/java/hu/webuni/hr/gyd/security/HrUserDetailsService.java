package hu.webuni.hr.gyd.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.model.HrUser;
import hu.webuni.hr.gyd.model.HrUserDetails;
import hu.webuni.hr.gyd.repository.EmployeeRepository;
import hu.webuni.hr.gyd.repository.HrUserRepository;

@Service
public class HrUserDetailsService implements UserDetailsService {
	
	@Autowired
	HrUserRepository userRepository;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Override
	public HrUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HrUser user = userRepository.findById(username).orElseThrow( () -> new UsernameNotFoundException(username) );
		Employee employee = employeeRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException(username) );
		HrUserDetails userDetails = new HrUserDetails(username, user.getPassword(), 
				user.getRoles().stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList()));
		userDetails.setEmployeeId(employee.getEmployeeId());
		userDetails.setEmployeeName(employee.getName());
		userDetails.setPrincipalId(employee.getPrincipal().getEmployeeId());
		userDetails.setPrincipalName(employee.getPrincipal().getName());
		List<String> employees = employeeRepository.findByPrincipalId(employee.getEmployeeId()).stream()
				.map(e -> e.getName())
				.collect(Collectors.toList());
		userDetails.setEmployees(employees);
		List<Long> employeeIds = employeeRepository.findByPrincipalId(employee.getEmployeeId()).stream()
				.map(e -> e.getEmployeeId())
				.collect(Collectors.toList());
		userDetails.setEmployeeIds(employeeIds);
		return userDetails;
	}

}
