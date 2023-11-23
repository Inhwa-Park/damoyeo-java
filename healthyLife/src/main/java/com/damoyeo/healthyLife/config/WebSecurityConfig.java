package com.damoyeo.healthyLife.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import com.damoyeo.healthyLife.security.JwtAuthenticationFilter;

import lombok.extern.log4j.Log4j2;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.cors()
		.and()
		.csrf()
		.disable()
		.httpBasic()
		.disable()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers(
				"/home/**", 
				"/caloryApi/**", 
				"/wiseSayingApi/**", 
				"/memberApi/signup/**",
				"/memberApi/signup/checkUserName/**",
				"/memberApi/signup/checkEmail/**", 
				"/memberApi/signin/**",
				"/memberApi/findPwd/**", 
				"/memberApi/findId/**",
				"/memberApi/pwdUpdate/**",
				"/memberApi/mailConfirm/**",
				"/coummunityApi/communityList/**",
				"/coummunityApi/communityListByType/**",
				"/coummunityApi/communityInfo/**",
				"/coummunityApi/addViews/**",
				"/coummunityApi/getImage/**",
				"/commentApi/getAll/**",
				"/shopApi/getAllProduct/**",
				"/shopApi/getAllSaleProduct/**",
				"/shopApi/getImage/**",
				"/shopApi/getProduct/**",
				"/shopApi/getAllByType/**",
				"/shopApi/getAllSaleByType/**",
				"/ws/chat",
				"/chat/**",
				"/",
				"/wiseSayingApi",
				"/static/**",
				"/manifest.json",
				"/favicon.ico",
				"/img/**",
				"/trainerApi/getImage/**",
				"/trainerApi/getAll/**",
				"/trainerApi/userOrTrainer/**",
				"/homeApi/getPosts/**",
				"/homeApi/getProducts/**",
				"/homeApi/getCalories/**"
				)
		
		.permitAll()
		.antMatchers("/adminApi/**").hasRole("ADMIN")
		.anyRequest()
		.authenticated();
		
		http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
	//return http.build();
	}
	
	@Bean()
	public UserDetailsService userDetailsService(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}
}
