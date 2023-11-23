package com.damoyeo.healthyLife.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.damoyeo.healthyLife.bean.Goal;
import com.damoyeo.healthyLife.bean.GoalCount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyDTO<T> {
	private Integer totalCount;
	private Integer scheduleCount;
	
	private List<Goal> totalList; //인화 추가 1026
	private List<Goal> goalListByClickDate; //인화 추가 1026
	
	
	public void SumTotalCount(List<Goal> list) {
		totalCount = list.size();
	}
		
}
