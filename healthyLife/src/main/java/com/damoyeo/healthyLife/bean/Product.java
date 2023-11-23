package com.damoyeo.healthyLife.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
	long id;
	String name;
	long price;
	boolean isSale;
	long discountPrice;
	String image;
	String description;
	String type;
}
