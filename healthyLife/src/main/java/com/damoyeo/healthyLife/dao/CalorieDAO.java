package com.damoyeo.healthyLife.dao;

import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Calorie;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CalorieDAO {
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

	public CalorieDAO() {
		super();
	}
	
	public List<Calorie> select() {
		List<Calorie> calories = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Calorie>> h = new BeanListHandler<>(Calorie.class);
			calories = qr.query(queryMap.get("selectCalory"), h);


		} catch (SQLException se) {
			se.printStackTrace();
		}
		return calories;
	}

	public List<Calorie> selectByType(String type) {
		List<Calorie> calories = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Calorie>> h = new BeanListHandler<>(Calorie.class);
			Object[] p = {type};
			calories = qr.query(queryMap.get("selectByTypeCalory"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return calories;
	}
	
	public List<Calorie> selectByTitle(String title){
		List<Calorie> calories = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Calorie>> h = new BeanListHandler<>(Calorie.class);
			Object[] p = {title};
			calories = qr.query(queryMap.get("selectByTitleCalory"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return calories;
	}
}
