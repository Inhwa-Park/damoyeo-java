package com.damoyeo.healthyLife.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ordering {
	long id;
	long productId;
	long memberId;
	Date orderDate;
	long salePrice;
	long amount;
	String payment;
	String address;
	String phone;
	String username;
	String zoneCode;
	String name;
}
