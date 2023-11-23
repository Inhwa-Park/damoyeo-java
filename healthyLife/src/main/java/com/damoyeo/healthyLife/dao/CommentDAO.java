package com.damoyeo.healthyLife.dao;

import org.springframework.stereotype.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.damoyeo.healthyLife.bean.Comment;

@Component
public class CommentDAO {
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
	public CommentDAO() {
		super();
	}
	
	public void insert(long listId, long memberId, String context) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<Comment> h = new BeanHandler<>(Comment.class);
			
			Date nowDate = new Date();
			Date today = DateUtils.truncate(nowDate, Calendar.DATE);
			
			Object[] p = {listId, today, memberId, context};
			qr.execute(queryMap.get("insertComment"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	
	public List<Object[]> select(long listId) {
		List<Object[]> comments = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Object[]>> h = new ArrayListHandler();
			Object[] p = {listId};
			comments = qr.query(queryMap.get("selectComment"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return comments;
	}
	
	public void delete(long listId) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			Object[] p = {listId};
			qr.execute(queryMap.get("deleteComment"), p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public List<Object[]> selectByMemberId(long memberId) {
		List<Object[]> comments = new ArrayList<>();
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Object[]>> h = new ArrayListHandler();
			Object[] p = {memberId};
			comments = qr.query(queryMap.get("selectCommentByUsername"), h, p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comments;
	}
	
	public void updateViews(long listId) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			Object[] p = {listId, listId};
			qr.execute(queryMap.get("updateViewsComment"), p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}

}
