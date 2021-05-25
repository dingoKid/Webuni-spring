package hu.webuni.hr.gyd.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.webuni.hr.gyd.configuration.HrConfigPropertiesWithLists;
import hu.webuni.hr.gyd.model.HrUserDetails;

@Service
public class JwtService {
	
	@Autowired
	HrConfigPropertiesWithLists config;
	
	public String createJwtToken(HrUserDetails principal) {
		return JWT.create()
		.withSubject(principal.getUsername())
		.withArrayClaim("auth", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
		.withClaim("name", principal.getEmployeeName())
		.withClaim("id", principal.getEmployeeId())
		.withArrayClaim("employees", principal.getEmployees() != null ? principal.getEmployees().toArray(String[]::new) : new String[0])
		.withArrayClaim("employeesIds", principal.getEmployeeIds() != null ? principal.getEmployeeIds().toArray(Long[]::new) : new Long[0])
		.withClaim("principalName", principal.getPrincipalName())
		.withClaim("principalId", principal.getPrincipalId())
		.withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(config.getExpiryInMinutes())))
		.withIssuer(config.getIssuer())
		.sign(Algorithm.HMAC256(config.getSecret()));	
	}
	
	public HrUserDetails parseJwt(String jwtToken) {
		DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC256(config.getSecret()))
			.withIssuer(config.getIssuer())
			.build()
			.verify(jwtToken);
		
		HrUserDetails result = new HrUserDetails(decodedJwt.getSubject(), "dummy", decodedJwt.getClaim("auth").asList(String.class)
				.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		
		result.setEmployeeId(decodedJwt.getClaim("id").asLong());
		result.setEmployeeName(decodedJwt.getClaim("name").asString());
		result.setEmployees(decodedJwt.getClaim("employees").asList(String.class));
		result.setEmployeeIds(decodedJwt.getClaim("employeeIds").asList(Long.class));
		result.setPrincipalId(decodedJwt.getClaim("principalId").asLong());
		result.setPrincipalName(decodedJwt.getClaim("principalName").asString());
		
		return result; 
	}

}
