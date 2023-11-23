package com.damoyeo.healthyLife.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.Schedule;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.dto.WeeklyDTO;
import com.damoyeo.healthyLife.service.GoalService;
import com.damoyeo.healthyLife.service.MemberService;
import com.damoyeo.healthyLife.service.SchedulerService;

@RestController
@RequestMapping("/goalApi")
public class GoalApiController {
	@Autowired
	private SchedulerService scheduleService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private GoalService goalService;
	
	Date nowDate = new Date();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	String strNowDate = simpleDateFormat.format(nowDate);
	
	
	@PostMapping("/addGoal/{AddGoalIdParam}") //스케쥴러 했다고 체크 후 반영된 값 반환 23.10.27 생성
	public ResponseEntity<ResponseDTO<WeeklyDTO>> AddGoal(@AuthenticationPrincipal GeneralUser gerneralUser, 
			@PathVariable long AddGoalIdParam) throws Exception{
		
		Member m = memberService.findAccount(gerneralUser.getId());
		goalService.addGoal(AddGoalIdParam, gerneralUser.getId());
		List<WeeklyDTO> viewList = new ArrayList<>();
		if(m != null) {
			WeeklyDTO<?> dto = new WeeklyDTO<>();
			
			List<Schedule> todoListByMenu = scheduleService.findTodoListByMenu(m.getId(), strNowDate);
			List<Schedule> todoListBySport = scheduleService.findTodoListBySport(m.getId(), strNowDate);
			List<Integer>counts=scheduleService.achievePercent(m.getId(),strNowDate); //rtn[int,int]반환됨
			
			dto.setTodoListByMenu(todoListByMenu);
			dto.setTodoListBySport(todoListBySport);
			dto.setCounts(counts);
		
						
			viewList.add(dto);
			ResponseDTO<WeeklyDTO> responseDTO = ResponseDTO.<WeeklyDTO>builder().data(viewList).build();
			
			return ResponseEntity.ok().body(responseDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("weeklySchedule loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	
	}
}
