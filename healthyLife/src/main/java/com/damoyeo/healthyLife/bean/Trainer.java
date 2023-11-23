package com.damoyeo.healthyLife.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Trainer {
	long id;
	
	String userName;
	String password;
	String experience;
	String introduce;
	String email;
	String img;
	
	long memberId;
	String token;
	
}
