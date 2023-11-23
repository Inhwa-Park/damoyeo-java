package com.damoyeo.healthyLife.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Goal;
import com.damoyeo.healthyLife.bean.GoalCount;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.Schedule;
import com.damoyeo.healthyLife.dto.MonthlyDTO;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.service.GoalService;
import com.damoyeo.healthyLife.service.MemberService;
import com.damoyeo.healthyLife.service.SchedulerService;


@RestController
@RequestMapping("/monthlySchedulerApi")
public class MonthlySchedulerApiController {
	@Autowired
	private SchedulerService scheduleService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private GoalService goalService;

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	//클릭한 날짜 골 목록 가져오기
	@GetMapping("/get/{dateParam}") 
	public ResponseEntity<ResponseDTO<MonthlyDTO>> get(@AuthenticationPrincipal GeneralUser generalUser, @PathVariable Date dateParam) throws Exception{

		Member m = memberService.findAccount(generalUser.getId());
		Date date = dateParam;
	
		if(m != null) {
			MonthlyDTO<?> dto = new MonthlyDTO<>();
			List<MonthlyDTO> viewList =  new ArrayList<>();
			 List<Goal> myGoalList = goalService.findByUserAndDate(m.getId(), date);
			 dto.setGoalListByClickDate(myGoalList);
			 viewList.add(dto);	
				
			ResponseDTO<MonthlyDTO> responseDTO = ResponseDTO.<MonthlyDTO>builder().data(viewList).build();
			return ResponseEntity.ok().body(responseDTO);
			
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("scheduler loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	//월별 골 테이블 전체 가져오기, 월간 성취율 계산
	@GetMapping("/getAll/{month}") 
	public ResponseEntity<ResponseDTO<MonthlyDTO>> getAll(@AuthenticationPrincipal GeneralUser generalUser, @PathVariable int month) throws Exception{

		Member m = memberService.findAccount(generalUser.getId());
	
		if(m != null) {
			MonthlyDTO<?> dto = new MonthlyDTO<>();
			List<MonthlyDTO> viewList =  new ArrayList<>();
			 List<Goal> totalList = goalService.find(m.getId(), month);	
			 dto.SumTotalCount(totalList);
			 dto.setScheduleCount(scheduleService.sumOfSchedule(m.getId()));
			 dto.setTotalList(totalList);			
			 viewList.add(dto);	
				
			ResponseDTO<MonthlyDTO> responseDTO = ResponseDTO.<MonthlyDTO>builder().data(viewList).build();
			return ResponseEntity.ok().body(responseDTO);
			
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("scheduler loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
