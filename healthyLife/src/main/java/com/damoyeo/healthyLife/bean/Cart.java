package com.damoyeo.healthyLife.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cart {
	long id;
	long productId;
	long memberId;
	long amount;
	long salePrice;
	String productName;
}
