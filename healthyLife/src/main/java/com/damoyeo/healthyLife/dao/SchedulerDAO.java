package com.damoyeo.healthyLife.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Schedule;

@Component
public class SchedulerDAO {
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
	
public SchedulerDAO() {
	super();
}

public List<Schedule> select() {
	List<Schedule> schedules = new ArrayList<>();

	try {
		QueryRunner qr = new QueryRunner(dataSource);
		ResultSetHandler<List<Schedule>> h = new BeanListHandler<>(Schedule.class);
		schedules = qr.query(queryMap.get("selectSchedule"), h);

	} catch (SQLException se) {
		se.printStackTrace();
	}
	return schedules;
}

public Schedule selectByType(String type) {
	Schedule s = new Schedule();

	try {
		QueryRunner qr = new QueryRunner(dataSource);
		ResultSetHandler<Schedule> h = new BeanHandler<>(Schedule.class);
		Object[] p = {type};
		s = qr.query(queryMap.get("selectByTypeSchedule"), h, p);

	} catch (SQLException se) {
		se.printStackTrace();
	}
	return s;
}

public List<Schedule> selectByMemberId(long memberId) {
	
	List<Schedule> scheduleList = new ArrayList<Schedule>();

	try {
		QueryRunner qr = new QueryRunner(dataSource);
		ResultSetHandler<List<Schedule>> h = new BeanListHandler<>(Schedule.class);
		Object[] p= {memberId};
		scheduleList = qr.query(queryMap.get("selectByMemberIdSchedule"), h, p);

	} catch (Exception e) {
		e.printStackTrace();
	} 
	return scheduleList;
}

public void insert(Schedule s) {
	try {
		QueryRunner qr = new QueryRunner(dataSource);
		Object[] p = {s.getMemberId(), s.getType(), s.getGoal(), s.getPeriod(), new Date(), s.getDueDate()};
		qr.execute(queryMap.get("insertSchedule"), p);
		
	}catch(SQLException se) {
		se.printStackTrace();
	}
}

public List<Schedule> selectAllTodoList(long memberId, String clickedDate) {
	List<Schedule> schedule = new ArrayList<>();
	Date clicked = java.sql.Date.valueOf(clickedDate);
	System.out.println(clicked);

	try {
		//이번주 첫일, 이번달 첫날 구하기
		String sunday = "";
		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date= simpleDateFormat.parse(clickedDate);
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		String startDate = simpleDateFormat.format(cal.getTime());
		System.out.println("startDate:"+startDate);
		
		QueryRunner qr = new QueryRunner(dataSource);
		ResultSetHandler<List<Schedule>> h = new BeanListHandler<>(Schedule.class);
		Object[] p= {startDate,clickedDate,memberId};
		schedule = qr.query(queryMap.get("selectJoinSchedule"), h, p);

	} catch (SQLException  se) {
		se.printStackTrace();
	}catch (Exception  e) {
		e.printStackTrace();
	}
	return schedule;
}
}
