package com.damoyeo.healthyLife.service;

import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.damoyeo.healthyLife.bean.Goal;
import com.damoyeo.healthyLife.bean.GoalCount;
import com.damoyeo.healthyLife.bean.Schedule;
import com.damoyeo.healthyLife.dao.GoalDAO;

@Service
public class GoalService {
	@Autowired
	GoalDAO goaldao;
	
	public GoalService() {
		goaldao = new GoalDAO();
	}
	
	public List<Goal> find(long memberId, int month){
		return goaldao.selectByMemberId(memberId, month);
	}
	
	public List<Goal> findByUserAndDate(long memberId, Date date) {
		Date tDate = DateUtils.truncate(date, Calendar.DATE);
		return goaldao.selectByMemberIdAndDate(memberId, tDate);
	}

	public List<GoalCount> findCountByAchieveDate(long memberId){
		return goaldao.selectCountByAchieveDate(memberId);
	}
	
	public void addGoal(long mid, long memberId) {
		goaldao.insert(mid, memberId);
	}
	public void removeGoal(long deleteGoalId) {
		goaldao.remove(deleteGoalId);
	}
	public List<Schedule> findReportList(long memberId) {
		return goaldao.selectByMemberId2(memberId);
	}
	public List<Schedule> findCountList(long scheduleId) {
		return goaldao.selectByScheduleId2(scheduleId);
	}
}
