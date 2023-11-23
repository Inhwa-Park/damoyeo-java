package com.damoyeo.healthyLife.dto;

import java.util.Date;
import java.util.List;
import com.damoyeo.healthyLife.bean.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyDTO<T> {
	String clickedDate;
	Date today;
	List<Schedule> todoListByMenu;
	List<Schedule> todoListBySport;
	List<Integer>counts;
	String weekOfYear;
	String monthOfYear;
	int num;
}
