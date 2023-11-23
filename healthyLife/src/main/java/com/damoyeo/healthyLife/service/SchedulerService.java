package com.damoyeo.healthyLife.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.Schedule;
import com.damoyeo.healthyLife.dao.SchedulerDAO;

@Service
public class SchedulerService {
	@Autowired
	SchedulerDAO scheduledao;
	
	public SchedulerService() {
		scheduledao = new SchedulerDAO();		
	}
	
	public List<Schedule> findAll() {
		return scheduledao.select();
	}

	public Schedule findByType(String type) {
		return scheduledao.selectByType(type);					
	}
	
	public List<Schedule> findByUser(long memberId) {		
		return scheduledao.selectByMemberId(memberId);
	}
	
	public synchronized void addSchedule(Schedule s) {
		scheduledao.insert(s);
	}
	
	public int sumOfSchedule(long memberId) {
		int rtn = 0;	
		List<Schedule> list = this.findByUser(memberId);
		Date tDate = DateUtils.truncate(new Date(), Calendar.DATE);
		
		int lastDay = 0;                                      
		Calendar cal = Calendar.getInstance();                       
		lastDay = cal.getActualMaximum(Calendar.DATE);
				
		for(Schedule s : list) {
			if(!s.getDueDate().before(tDate)) {
				if(s.getPeriod() == 1) {
					rtn += lastDay;
				}else if(s.getPeriod() == 7) {
					rtn += 5;
				}else if(s.getPeriod() == 30) {
					rtn += 1;
				}	
			}			
		}	
		return rtn;		
	}
	
	public String findOneWeek(String strNowDate) { // 선택된 날의 주차정보 구하는 함수
		String weekOfYear = "";
		try {	
			GregorianCalendar cal = new GregorianCalendar();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date= simpleDateFormat.parse(strNowDate);
			cal.setTime(date);
			
			weekOfYear = Integer.toString(cal.get(Calendar.WEEK_OF_YEAR));	
			
			
			} catch (Exception e) {
				e.getStackTrace();
			}
		return weekOfYear;
	}
	public List<Schedule>getCheckList(long memberId, String clickedDate){//오늘 checked disabled 판단유무 리스트
		List<Schedule> rtn = new ArrayList<Schedule>();
		List<Schedule> allList = scheduledao.selectAllTodoList(memberId, clickedDate);
		String thisweek = findOneWeek(clickedDate);
		for (Schedule s : allList) {
			String week = s.getWeek();
			rtn.add(s);	
			
				
				
			
		}
		return rtn;
	}
	public List<Integer> achievePercent(long memberId, String clickedDate) { // 1주일 달력중 하루를 눌렀을때 목표 모두 출력하는 함수
		List<Integer> rtn = new ArrayList<Integer>();
		List<Schedule> allList = scheduledao.selectAllTodoList(memberId,clickedDate);
		Date clicked = java.sql.Date.valueOf(clickedDate);
		String thisweek = findOneWeek(clickedDate);
		System.out.println("thisweek: "+thisweek);
		int falseCount = 0;
		int allCount = 0;
		int lastWeekTrueCount = 0;
		for (Schedule s : allList) {
			Date dueDate = s.getDueDate();
			Date inDate = s.getInputDate();
			String week = s.getWeek();
			long period = s.getPeriod();
			boolean checked = s.isChecked();

			if (dueDate.after(clicked) == true) {
				if (inDate.before(clicked) == true || inDate.equals(clicked) == true) {
					allCount = allCount + 1;
					if((checked == false)||(week.equals(thisweek) == false)) { //주차가 다른경우
						falseCount = falseCount + 1;
					}
					if((checked == true)&&(week.equals(thisweek) == true)) { 
						lastWeekTrueCount = lastWeekTrueCount + 1;
					}
				}
			}
		

		}
		int trueCount = allCount - falseCount;
		rtn.add(trueCount);
		rtn.add(falseCount);
		rtn.add(allCount);
		rtn.add(lastWeekTrueCount);
		System.out.println("rtn:"+rtn);
		return rtn;
	}
	
	public List<Schedule> findTodoListByMenu(long memberId, String clickedDate) { //1주일 달력중 하루를 눌렀을때 목표 모두 출력하는 함수
		List<Schedule> rtn= new ArrayList<>();
		List<Schedule> allList = scheduledao.selectAllTodoList(memberId, clickedDate);
		Date clicked = java.sql.Date.valueOf(clickedDate); 
		
		for(Schedule s :allList) {
			Date dueDate = s.getDueDate();
			Date inDate = s.getInputDate();
			String type = s.getType();
			//boolean checked = s.isChecked();
			
			if(type.equals("식단")) {
				if(inDate.before(clicked)==true || inDate.equals(clicked)==true) { //오늘이나 어제부터 시작한 목표들만 노출..
					if(dueDate.after(clicked)==true) { //date1이 현재보다 과거일때 true반환.
						rtn.add(s);
					}
				}
			}
		}
		return rtn;	
	}
	
	public List<Schedule> findTodoListBySport(long memberId, String clickedDate) { //1주일 달력중 하루를 눌렀을때 목표 모두 출력하는 함수
		List<Schedule> rtn= new ArrayList<>();
		List<Schedule> allList = scheduledao.selectAllTodoList(memberId, clickedDate);
		Date clicked = java.sql.Date.valueOf(clickedDate); 
		
		for(Schedule s :allList) {
			Date dueDate = s.getDueDate();
			Date inDate = s.getInputDate();
			String type = s.getType();
			if(type.equals("운동")) {
				if(inDate.before(clicked)==true || inDate.equals(clicked)==true) { //오늘이나 어제부터 시작한 목표들만 노출..
					if(dueDate.after(clicked)==true) { //date1이 현재보다 과거일때 true반환.
						rtn.add(s);
						
					}
				}
			}
		}
		return rtn;	
	}
}
