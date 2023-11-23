package com.damoyeo.healthyLife.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.PostMessage;
import com.damoyeo.healthyLife.bean.Schedule;

@Component
public class PostMessageDAO {
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
	public PostMessageDAO() {
		
	}
	public void update(PostMessage pmg){
	
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<PostMessage>> h = new BeanListHandler<>(PostMessage.class);
			Object[] p = {pmg.getRecieveName(), pmg.getSenderName(), pmg.getTitle(), pmg.getContent(), 
					pmg.getDate(),pmg.getTrainerId(), pmg.getPostMsgId()};
			
			qr.execute(queryMap.get("updatePmg"),h, p);
			
			}catch(SQLException se) {
				se.printStackTrace();
			}
		
	
	}
	public List<PostMessage>SelectByPostMsgId(long postMsgId){
		List<PostMessage> rtn =  new ArrayList<>();
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<PostMessage>> h = new BeanListHandler<>(PostMessage.class);
			Object[] p = {postMsgId};
			rtn = qr.query(queryMap.get("SelectByPostMsgId"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return rtn;
	}
	public List<PostMessage>SelectByUserId(long userId){
		List<PostMessage> rtn =  new ArrayList<>();
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<PostMessage>> h = new BeanListHandler<>(PostMessage.class);
			Object[] p = {userId,userId};
			rtn = qr.query(queryMap.get("selectPmgByUserId"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return rtn;
	}
	public void delete(long id) {
		try {
		QueryRunner qr = new QueryRunner(dataSource);
		
		Object[] p = {id};
		qr.execute(queryMap.get("deletePmg"), p);
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	public List<PostMessage>Select(){
		List<PostMessage> rtn =  new ArrayList<>();
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<PostMessage>> h = new BeanListHandler<>(PostMessage.class);
			rtn = qr.query(queryMap.get("selectPmg"), h);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return rtn;
	}
	public void insert(PostMessage pmg) {
		try {
		QueryRunner qr = new QueryRunner(dataSource);
		
		Object[] p = {pmg.getRecieveName(), pmg.getSenderName(), pmg.getTitle(), pmg.getContent(), pmg.getDate(), pmg.getMemberId(), pmg.getTrainerId()};
		System.out.println( pmg.getDate());
		qr.execute(queryMap.get("insertPmg"), p);
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
}
