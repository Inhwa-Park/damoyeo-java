package com.damoyeo.healthyLife.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.AdminUser;
import com.damoyeo.healthyLife.bean.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class TokenProvider {
	private static final String SECRET_KEY = "dufthlfmsjfkjdlkfjlakji14SSGFJ04sawhTPiD";
	public String create(Member userEntity) {
		Date expiryDate = Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
		String rtn = Jwts.builder()
		.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
		.setSubject(Long.toString(userEntity.getId()) )
		.claim("role", userEntity.getRole())
		.setIssuer("demo app")
		.setIssuedAt(new Date())
		.setExpiration(expiryDate)
		.compact();
		return rtn;
	}
	
	public String createAdmin(AdminUser admin) {
		Date expiryDate = Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
		System.out.println("role In tokenProvider: "+ admin.getRole());
		String rtn = Jwts.builder()
		.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
		.setSubject(admin.getId())
		.claim("role", admin.getRole())
		.setIssuer("demo app")
		.setIssuedAt(new Date())
		.setExpiration(expiryDate)
		.compact();
		return rtn;
	}
	
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
}
