package hu.webuni.hr.gyd.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
	
//	private final String SECRET = config.getSecret();	
//	private final String ISSUER = config.getIssuer();
	

	public String createJwtToken(HrUserDetails principal) {
		return JWT.create()
		.withSubject(principal.getUsername())
		.withArrayClaim("auth", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
		.withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2)))
		.withIssuer("KD")
		.sign(Algorithm.HMAC256("nincs"));		
	}
	
	public UserDetails parseJwt(String jwtToken) {
		DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC256("nincs"))
		.withIssuer("KD")
		.build()
		.verify(jwtToken);
		return new User(decodedJwt.getSubject(), "dummy", decodedJwt.getClaim("auth").asList(String.class)
				.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
	}

}
