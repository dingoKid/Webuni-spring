package hu.webuni.hr.gyd.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.webuni.hr.gyd.model.Employee;
import hu.webuni.hr.gyd.repository.EmployeeRepository;

@Service
public class HrUserDetailsService implements UserDetailsService {
	
	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee user = employeeRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException(username) );
		return new User(username, user.getPassword(), 
				user.getRoles().stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList()));
	}

}
