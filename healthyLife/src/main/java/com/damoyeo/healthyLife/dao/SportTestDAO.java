package com.damoyeo.healthyLife.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Sport;
import com.damoyeo.healthyLife.bean.Trainer;

@Component
public class SportTestDAO {

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

	public SportTestDAO() {
		super();
	}

	public List<Sport> select() {
		List<Sport> sports = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Sport>> h = new BeanListHandler<>(Sport.class);
			sports = qr.query(queryMap.get("selectSport"), h);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return sports;
	}

	public Sport selectByTitle(String title) {
		Sport s = new Sport();

		try {
			if (title.equals("테스트를 통해 나의 운동유형을 알아보세요!")) {
				return new Sport(-1, "", "", "", "");

			} else {
				QueryRunner qr = new QueryRunner(dataSource);
				ResultSetHandler<Sport> h = new BeanHandler<>(Sport.class);
				Object[] p = { title };
				s = qr.query(queryMap.get("selectByTitleSport"), h, p);
			}

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return s;
	}

}