package com.damoyeo.healthyLife.dao;

import java.io.IOException;
import java.sql.Connection;
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

import com.damoyeo.healthyLife.bean.WiseSaying;


@Component
public class WiseSayingDAO {
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
	
	public WiseSayingDAO() {
		super();
	}
	
	public List<WiseSaying> select() {
		List<WiseSaying> wisesayings = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<WiseSaying>> h = new BeanListHandler<>(WiseSaying.class);
			wisesayings = qr.query(queryMap.get("selectWiseSaying"), h);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return wisesayings;
	}

}
