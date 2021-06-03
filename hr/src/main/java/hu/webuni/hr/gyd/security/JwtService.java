package hu.webuni.hr.gyd.security;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.webuni.hr.gyd.configuration.HrConfigPropertiesWithLists;
import hu.webuni.hr.gyd.model.HrUserDetails;

@Service
public class JwtService {
	
	private static final String EMPLOYEE_IDS = "employeeIds";
	private static final String EMPLOYEES = "employees";
	private static final String PRINCIPAL_ID = "principalId";
	private static final String PRINCIPAL_NAME = "principalName";
	private static final String ID = "id";
	private static final String USERNAME = "username";
	private static final String AUTH = "auth";
	private Algorithm algorithm;
	
	@Autowired
	HrConfigPropertiesWithLists config;
	
	@PostConstruct
	public void init() {
		try {
			algorithm = (Algorithm) Algorithm.class.getMethod(config.getAlgorithm(), String.class).invoke(Algorithm.class, config.getSecret());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException();
		} 
	}
	
	public String createJwtToken(HrUserDetails principal) {
		
		Builder jwtBuilder = JWT.create()		
			.withSubject(principal.getUsername())
			.withArrayClaim(AUTH, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
			.withClaim(USERNAME, principal.getEmployeeName())
			.withClaim(ID, principal.getEmployeeId())
			.withClaim(PRINCIPAL_NAME, principal.getPrincipalName())
			.withClaim(PRINCIPAL_ID, principal.getPrincipalId());
		
		if(principal.getEmployees() != null) {
			jwtBuilder
				.withArrayClaim(EMPLOYEES, principal.getEmployees().toArray(String[]::new));			
		}
		
		if(principal.getEmployeeIds() != null) {
			jwtBuilder
				.withArrayClaim(EMPLOYEE_IDS, principal.getEmployeeIds().toArray(Long[]::new));
		}
		
		return jwtBuilder
			.withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(config.getExpiryInMinutes())))
			.withIssuer(config.getIssuer())
			.sign(algorithm);
	}
	
	public HrUserDetails parseJwt(String jwtToken) {
		DecodedJWT decodedJwt = JWT.require(algorithm)
			.withIssuer(config.getIssuer())
			.build()
			.verify(jwtToken);
		
		HrUserDetails result = new HrUserDetails(decodedJwt.getSubject(), "dummy", decodedJwt.getClaim(AUTH).asList(String.class)
				.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		
		result.setEmployeeId(decodedJwt.getClaim(ID).asLong());
		result.setEmployeeName(decodedJwt.getClaim(USERNAME).asString());
		result.setEmployees(decodedJwt.getClaim(EMPLOYEES).asList(String.class));
		result.setEmployeeIds(decodedJwt.getClaim(EMPLOYEE_IDS).asList(Long.class));
		result.setPrincipalId(decodedJwt.getClaim(PRINCIPAL_ID).asLong());
		result.setPrincipalName(decodedJwt.getClaim(PRINCIPAL_NAME).asString());
		
		return result;
	}

}
