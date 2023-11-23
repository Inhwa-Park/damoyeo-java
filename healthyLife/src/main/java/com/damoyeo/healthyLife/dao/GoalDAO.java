package com.damoyeo.healthyLife.dao;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.damoyeo.healthyLife.bean.Goal;
import com.damoyeo.healthyLife.bean.GoalCount;
import com.damoyeo.healthyLife.bean.Schedule;


@Component
public class GoalDAO {
	private DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private Map<String, String> queryMap;

	@Autowired
	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
	}
	public GoalDAO() {
		super();
	}
	
	public List<Goal> selectByMemberId(long memberId, int month) {
		List<Goal> goals = new ArrayList<>();
				
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Goal>> h = new BeanListHandler<>(Goal.class);
			Object[] p = {memberId, month};
			goals = qr.query(queryMap.get("selectByMemberIdGoal"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return goals;
	}

	public Goal selectByScheduleId(int scheduleId) {
		Goal c = new Goal();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<Goal> h = new BeanHandler<>(Goal.class);
			Object[] p = {scheduleId};
			c = qr.query(queryMap.get("selectByScheduleIdGoal"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return c;
	}
	
	public List<Goal> selectByMemberIdAndDate(long memberId, Date date) {
		List<Goal> goals = new ArrayList<>();
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Goal>> h = new BeanListHandler<>(Goal.class);
			Object[] p = {memberId, date};
			goals = qr.query(queryMap.get("selectByMemberIdAndDateGoal"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return goals;
	}
	
	public List<GoalCount> selectCountByAchieveDate(long memberId){
		List<GoalCount> goals = new ArrayList<>();		
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<GoalCount>> h = new BeanListHandler<>(GoalCount.class);
			Object[] p = {memberId};
			goals = qr.query(queryMap.get("selectCountByAchieveDateGoal"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}			
		return goals;
	}
	
	public void insert(long mid, long memberId) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<Goal> h = new BeanHandler<>(Goal.class);
			Object[] p = {mid, new Date(), memberId};
			qr.execute(queryMap.get("insertGoal"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	public void remove(long deleteGoalId) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<Goal> h = new BeanHandler<>(Goal.class);
			Object[] p = {deleteGoalId, deleteGoalId};
			qr.execute(queryMap.get("removeGoal"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	public List<Schedule> selectByMemberId2(long memberId) {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Schedule>> h = new BeanListHandler<>(Schedule.class);
			Object[] p = {memberId};
			scheduleList = qr.query(queryMap.get("selectByMemberId2Goal"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}		
		return scheduleList;
	}
	
	public List<Schedule> selectByScheduleId2(long scheduleId){
		List<Schedule> goals = new ArrayList<>();		
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Schedule>> h = new BeanListHandler<>(Schedule.class);
			Object[] p = {scheduleId};
			goals = qr.query(queryMap.get("selectByScheduleId2Goal"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}			
		return goals;
	}
}
