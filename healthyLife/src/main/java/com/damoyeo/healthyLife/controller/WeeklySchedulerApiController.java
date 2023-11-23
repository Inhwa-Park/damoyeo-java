package com.damoyeo.healthyLife.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import org.springframework.web.bind.annotation.RequestBody;
import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.Schedule;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.dto.WeeklyDTO;
import com.damoyeo.healthyLife.service.GoalService;
import com.damoyeo.healthyLife.service.MemberService;
import com.damoyeo.healthyLife.service.SchedulerService;


@RestController
@RequestMapping("/weeklySchedulerApi")
public class WeeklySchedulerApiController {
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private GoalService goalService;
	
	@PostMapping("/deleteSchedule/{deleteIdParam}") //스케쥴러 삭제
	public ResponseEntity<ResponseDTO<WeeklyDTO>> deleteSchedule(
			@AuthenticationPrincipal GeneralUser gerneralUser, @PathVariable long deleteIdParam) throws Exception{
		
		Date nowDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strNowDate = simpleDateFormat.format(nowDate);
		Member m = memberService.findAccount(gerneralUser.getId());
		List<WeeklyDTO> viewList = new ArrayList<>();
		
		if(m != null) {
			goalService.removeGoal(deleteIdParam);
			WeeklyDTO<?> dto = new WeeklyDTO<>();
			
			List<Schedule> todoListByMenu = schedulerService.findTodoListByMenu(m.getId(), strNowDate);
			List<Schedule> todoListBySport = schedulerService.findTodoListBySport(m.getId(), strNowDate);
			List<Integer>counts=schedulerService.achievePercent(m.getId(),strNowDate); //rtn[int,int]반환됨
			
			dto.setTodoListByMenu(todoListByMenu);
			dto.setTodoListBySport(todoListBySport);
			dto.setCounts(counts);
			viewList.add(dto);
			
			ResponseDTO<WeeklyDTO> responseDTO = ResponseDTO.<WeeklyDTO>builder().data(viewList).msg("스케쥴이 정상적으로 삭제 되었습니다.").build();
			return ResponseEntity.ok().body(responseDTO);
			
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("weeklySchedule loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/addSchedule") //스케쥴러 추가
	public ResponseEntity<ResponseDTO<WeeklyDTO>> addSchedule(
			@AuthenticationPrincipal GeneralUser gerneralUser, @RequestBody Schedule s) throws Exception{
		
		Date nowDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strNowDate = simpleDateFormat.format(nowDate);
		Member m = memberService.findAccount(gerneralUser.getId());
		Schedule schedule = new Schedule();
		List<WeeklyDTO> viewList = new ArrayList<>();
		
		if(m != null) {
			WeeklyDTO<?> dto = new WeeklyDTO<>();
			
			schedule.setMemberId(m.getId());
			schedule.setType(s.getType());
			schedule.setGoal(s.getGoal());
			schedule.setPeriod(s.getPeriod());
			schedule.setDueDate(s.getDueDate());
			schedulerService.addSchedule(schedule);
			
			List<Schedule> todoListByMenu = schedulerService.findTodoListByMenu(m.getId(), strNowDate);
			List<Schedule> todoListBySport = schedulerService.findTodoListBySport(m.getId(), strNowDate);
			List<Integer>counts=schedulerService.achievePercent(m.getId(),strNowDate);
			
			dto.setTodoListByMenu(todoListByMenu);
			dto.setTodoListBySport(todoListBySport);
			dto.setCounts(counts);
			viewList.add(dto);
			
			ResponseDTO<WeeklyDTO> responseDTO = ResponseDTO.<WeeklyDTO>builder().data(viewList).msg("스케쥴이 정상적으로 추가 되었습니다.").build();
			return ResponseEntity.ok().body(responseDTO);
			
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("weeklySchedule loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	
	
	
	@GetMapping("/getAll/{dateParam}") 
	public ResponseEntity<ResponseDTO<WeeklyDTO>>  getAll(
			@AuthenticationPrincipal GeneralUser gerneralUser, @PathVariable String dateParam) throws Exception{
		//23.10.26dateforweek 를 strNow로 바꾸면 될꺼 같지만 아직 요즘 날짜로 투두가 없어서 테스트 해보기 어려움. 해당 내용 고려해야함
		
		Date nowDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strNowDate = simpleDateFormat.format(nowDate);
		Member m = memberService.findAccount(gerneralUser.getId());
		String dateForWeek =  StringUtils.defaultIfEmpty(dateParam, strNowDate); //파라미터로 받던 dateforWeek없애고 여기로 대체
		Date date = simpleDateFormat.parse(StringUtils.defaultIfEmpty(dateParam, strNowDate));
		System.out.println("date: "+date );
		Date today = DateUtils.truncate(simpleDateFormat.parse(dateForWeek), Calendar.DATE);
		
		List<WeeklyDTO> viewList = new ArrayList<>();
		if(m != null) {
			WeeklyDTO<?> dto = new WeeklyDTO<>();
			
			List<Schedule> todoListByMenu = schedulerService.findTodoListByMenu(m.getId(), dateForWeek);
			List<Schedule> todoListBySport = schedulerService.findTodoListBySport(m.getId(), dateForWeek);
			List<Integer>counts=schedulerService.achievePercent(m.getId(),dateForWeek); //rtn[int,int]반환됨
			String weekOfYear = schedulerService.findOneWeek(dateForWeek);
			String monthOfYear = "";
			String D[] = dateForWeek.split("-"); 
			for(int i=0 ; i<D.length ; i++) { monthOfYear=D[1]; }
			
			int num = date.compareTo(today); //today가 date날보다 이전이면 음수값리턴, 이후면 양수, 같으면 0 
			
			dto.setClickedDate(dateForWeek);
			dto.setToday(today);
			dto.setTodoListByMenu(todoListByMenu);
			dto.setTodoListBySport(todoListBySport);
			dto.setCounts(counts);
			dto.setWeekOfYear(weekOfYear);
			dto.setMonthOfYear(monthOfYear);
			dto.setNum(num);			
			viewList.add(dto);
			
			ResponseDTO<WeeklyDTO> responseDTO = ResponseDTO.<WeeklyDTO>builder().data(viewList).build();
			return ResponseEntity.ok().body(responseDTO);
			
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("weeklySchedule loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
