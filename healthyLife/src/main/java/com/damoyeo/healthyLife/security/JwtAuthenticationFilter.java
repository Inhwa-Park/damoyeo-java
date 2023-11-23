package com.damoyeo.healthyLife.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.damoyeo.healthyLife.service.AdminService;
import com.damoyeo.healthyLife.service.MemberService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	TokenProvider tokenProvider;
	@Autowired
	AdminService adminService;
	@Autowired
	private MemberService memberService;
	
	//서블릿 필터
	@Override 
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
		try {
			String token = parseBearerToken(request);
			if(token != null && !token.equalsIgnoreCase("null")) {
				String userId = tokenProvider.validateAndGetUserId(token);
		
				if(userId.equals("1")) {//관리자
					String usernameAdmin = adminService.findUsernameById(userId);
					UserDetails userDetails = adminService.loadUserByUsername(usernameAdmin);
					List<GrantedAuthority> authorities = getAuthoritiesFromToken(token); // 권한을 토큰에서 가져오는 메서드 호출
					log.info("Authenticated userId: " + userId);
					AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							authorities
							);
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
					securityContext.setAuthentication(authentication);
					SecurityContextHolder.setContext(securityContext);
				}else {
					//일반 회원인 경우
					String username = memberService.findUsernameById(userId);
					UserDetails userDetails = memberService.loadUserByUsername(username);
					System.out.println("userDetails: "+userDetails);
					
					
					List<GrantedAuthority> authorities = getAuthoritiesFromToken(token); // 권한을 토큰에서 가져오는 메서드 호출
					log.info("Authenticated userId: " +userId);
					
					
					AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							authorities
							);
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
					securityContext.setAuthentication(authentication);
					System.out.println("securityContext: "+securityContext);
					SecurityContextHolder.setContext(securityContext);
				}	
			}
		}catch(Exception e) {
			//response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			log.info("Could not set user authentication in security context", e);
		}
		filterChain.doFilter(request, response);
		
	}
	
	private String parseBearerToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
	private List<GrantedAuthority> getAuthoritiesFromToken(String token) {
		String secretKey = "dufthlfmsjfkjdlkfjlakji14SSGFJ04sawhTPiD";
		// 토큰에서 권한 정보를 추출하는 로직을 작성해야 합니다.
        // 예를 들어, tokenProvider.validateAndGetAuthorities(token)과 같은 메서드를 사용하여 권한을 가져올 수 있습니다.
        // 이 예시에서는 권한이 "ROLE_USER"와 "ROLE_ADMIN" 두 가지로 가정합니다.
        List<GrantedAuthority> authorities = new ArrayList<>();
       
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        System.out.println("claims: "+claims);
        
        
        String role = (String) claims.get("role");
        System.out.println("role: "+role);
        
        
        if (role != null && role.equals("TRUE")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        System.out.println("authorities: "+authorities);
        return authorities;
    }
}
