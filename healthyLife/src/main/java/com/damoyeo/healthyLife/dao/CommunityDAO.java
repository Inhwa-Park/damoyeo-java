package com.damoyeo.healthyLife.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.bean.Ordering;

@Component
public class CommunityDAO {
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

	public CommunityDAO() {
		super();
	}
	
	public List<Object[]> select() {
		List<Object[]> communities = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Object[]>> h = new ArrayListHandler();
			communities = qr.query(queryMap.get("selectCommunity"), h);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return communities;
	}
	
	public List<Object[]> selectByPro() {
		List<Object[]> communities = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Object[]>> h = new ArrayListHandler();
			Object[] p = {"전문"};
			communities = qr.query(queryMap.get("selectByTypeCommunity"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return communities;
	}
	
	public List<Object[]> selectByViews() { //인화 추가 1025
		List<Object[]> communities = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Object[]>> h = new ArrayListHandler();
			communities = qr.query(queryMap.get("selectByViews"), h);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return communities;
	}
	
	public List<Community> selectByViewsHome() { //인화 추가 1108
		List<Community> communities = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Community>> h = new BeanListHandler<>(Community.class);
			communities = qr.query(queryMap.get("selectByViewsHome"), h);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return communities;
	}
	
	public List<Object[]> selectByPeople() {
		List<Object[]> communities = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Object[]>> h = new ArrayListHandler();
			Object[] p = {"일반"};
			communities = qr.query(queryMap.get("selectByTypeCommunity"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return communities;
	}

	public Object[] selectById(long listId) {
		Object[] c = null;

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<Object[]> h = new ArrayHandler();
			Object[] p = {listId};
			c = qr.query(queryMap.get("selectByIdCommunity"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return c;
	}
	
	public void insert(Community c) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ScalarHandler<Long> h = new ScalarHandler<>();
			Object[] p = {c.getContent(), c.getImage(), c.getDate(), c.getMemberId(), c.getTitle(), 0, 0};
			long num = qr.insert(queryMap.get("insertCommunity"), h, p);
			c.setId(num); 
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public void update(Community c) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			Object[] p = {c.getContent(), c.getImage(), c.getDate(), c.getTitle(), c.getId()};
			qr.execute(queryMap.get("updateCommunity"), p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public void delete(long listId) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			Object[] p = {listId};
			qr.execute(queryMap.get("deleteCommunity"),p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public List<Object[]> getSearchByUserName(String searchContent) { //0823 용주작성
		List<Object[]> c = null;
		try {
				searchContent = searchContent.trim();
				QueryRunner qr = new QueryRunner(dataSource);
				ResultSetHandler<List<Object[]>> h = new ArrayListHandler();
				Object[] p = {searchContent};
				c = qr.query(queryMap.get("selectBySearchByUsername"), h, p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
		return c;
	}
	
	public List<Object[]> getSearchByContent(String searchContent) { //0821 용주작성
		List<Object[]> c = null;
		
		try {
				searchContent = searchContent.trim();
				QueryRunner qr = new QueryRunner(dataSource);
				ResultSetHandler<List<Object[]>> h = new ArrayListHandler();
				Object[] p = {searchContent};
				c = qr.query(queryMap.get("selectBySearchByContent"), h, p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
		return c;
	}
	
	public List<Object[]> getSearchByUserDate(String searchContent) { //0823 용주작성
		List<Object[]> c = null;
		try {
				searchContent = searchContent.trim();
				QueryRunner qr = new QueryRunner(dataSource);
				ResultSetHandler<List<Object[]>> h = new ArrayListHandler();
				Object[] p = {searchContent};
				c = qr.query(queryMap.get("selectBySearchByDate"), h, p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
		return c;
	}
	
	public Community selectByCommunityId(long listId) {
		Community c = new Community();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<Community> h = new BeanHandler<>(Community.class);
			Object[] p = {listId};
			c = qr.query(queryMap.get("selectByCommunityId"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return c;
	}
	
	public void updateViews(long listId) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			Object[] p = {listId, listId};
			qr.execute(queryMap.get("updateViews"), p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
}
