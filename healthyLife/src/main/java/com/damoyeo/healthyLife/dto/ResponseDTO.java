package com.damoyeo.healthyLife.dto;



import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO<T> { //제네릭 선언
	private String error;
	private String msg;
	private List<T> data;
	private Object object;
}
