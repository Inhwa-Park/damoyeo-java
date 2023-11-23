package com.damoyeo.healthyLife.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.Schedule;
import com.damoyeo.healthyLife.bean.Trainer;

@Component
public class TrainerDAO {
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

	public TrainerDAO() {
		super();
	}
	
	public Long selectByUsername(String username)  throws Exception{
		Long rtn = null;
		Connection conn = dataSource.getConnection();

		String sql = queryMap.get("findByUsernameTrainer");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, username);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				rtn = rs.getLong("memberid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	public List<Trainer> select() {
		List<Trainer> trainers = new ArrayList<>();

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Trainer>> h = new BeanListHandler<>(Trainer.class);
			trainers = qr.query(queryMap.get("selectTrainer"), h);
			System.out.println("trainers: "+ trainers);
			} catch (SQLException se) {
			se.printStackTrace();
		}
		return trainers;
	}
	
	public void insertInfo(Trainer trainer) throws Exception {

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			Object[] p = {trainer.getExperience(),trainer.getIntroduce(),trainer.getImg(), trainer.getId()};
			qr.execute(queryMap.get("insertInfoTrainer"), p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Trainer findAccountById(long userId) throws Exception {
		
		Trainer trainer = new Trainer();
		Connection conn = dataSource.getConnection();

		String sql = queryMap.get("findByIdMember");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, userId);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				trainer.setId(rs.getLong("id"));
				trainer.setUserName(rs.getString("username"));
				trainer.setPassword(rs.getString("password"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return trainer;
	}
	
	public String findUserOrTrainer(long userId) throws Exception {
		String rtn = "FALSE";
		Connection conn = dataSource.getConnection();

		String sql = queryMap.get("findByIdTrainer");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, userId);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			if(rs.next()) { 
				rtn = rs.getString("result");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	public void addTrainer(Trainer trainer) throws Exception { //회원가입
		Connection conn = dataSource.getConnection();
		String sql = queryMap.get("insertTrainer");
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, trainer.getUserName());
			pstmt.setString(2, trainer.getPassword());
			pstmt.setLong(3, trainer.getMemberId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProfile(String p, long pro) {
		
		try {
			Connection conn = dataSource.getConnection();
			String sql = queryMap.get("updateProfile");
			PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, p);
			pstmt.setLong(2, pro);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
