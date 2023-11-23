package com.damoyeo.healthyLife.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.AdminUser;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class AdminUserDAO {
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
	public AdminUser selectByAdminUsername(String username) throws Exception {
		AdminUser admin = new AdminUser();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<AdminUser> h = new BeanHandler<>(AdminUser.class);
			Object[] p = {username};
			admin = qr.query(queryMap.get("selectByUsernameAdmin"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return admin;
	}
	public AdminUser selectByAdminUserId(String id) throws Exception {
		AdminUser admin = new AdminUser();
		Connection conn = dataSource.getConnection();
		String sql = queryMap.get("selectByIdAdmin");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, Long.valueOf(id));
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				admin.setId(String.valueOf(rs.getLong("id")));
				admin.setAdminName(rs.getString("userName"));
				admin.setPassword(rs.getString("password"));
				admin.setRole(rs.getString("role"));
			}

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return admin;
	}

}
