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
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Menu;


@Component
public class MenuTestDAO {
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
	public MenuTestDAO() {
		super();
	}

	public List<Menu> select() {
		List<Menu> menus = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Menu>> h = new BeanListHandler<>(Menu.class);
			menus = qr.query(queryMap.get("select"), h);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return menus;
	}

	public Menu selectByTitle(String title) {
		Menu m = new Menu();

		try {
			if (title.equals("테스트를 통해 나의 식단유형을 알아보세요!")) {
				return new Menu(-1, "", "", "", "");

			} else {
				QueryRunner qr = new QueryRunner(dataSource);
				ResultSetHandler<Menu> h = new BeanHandler<>(Menu.class);
				Object[] p = { title };
				m = qr.query(queryMap.get("selectByTitle"), h, p);

			}

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return m;
	}
}
