package com.damoyeo.healthyLife.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryLoader;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Member;

import lombok.Data;

@Component
public class MemberDAO {
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
	
   public MemberDAO() {
		super();
	}

   public List<Member> select() throws Exception {
		Connection conn = dataSource.getConnection();
		List<Member> members = new ArrayList<>();

		String sql = queryMap.get("selectMember");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				Member m = new Member();
				m.setId(rs.getLong("id"));
				m.setUsername(rs.getString("userName"));
				m.setPassword(rs.getString("password"));
				m.setEmail(rs.getString("email"));
				m.setMenuType(rs.getString("menuType"));
				m.setSportType(rs.getString("sportType"));
				m.setType(rs.getString("type"));
				m.setImg(rs.getString("img"));

				members.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return members;
	}
   
   public List<Member> selectAllByType(String type) throws Exception {
		Connection conn = dataSource.getConnection();
		List<Member> members = new ArrayList<>();

		String sql = queryMap.get("selectAllByType");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, type);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				Member m = new Member();
				m.setId(rs.getLong("id"));
				m.setUsername(rs.getString("userName"));
				m.setPassword(rs.getString("password"));
				m.setEmail(rs.getString("email"));
				m.setMenuType(rs.getString("menuType"));
				m.setSportType(rs.getString("sportType"));
				m.setType(rs.getString("type"));
				m.setImg(rs.getString("img"));

				members.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return members;
	}

	public void deleteAccountById(long userId) throws Exception {
		Connection conn = dataSource.getConnection();

		String sql = queryMap.get("deleteByIdMember");
		PreparedStatement pstmt = conn.prepareStatement(sql);

		try (conn; pstmt) {
			pstmt.setLong(1, userId);
			pstmt.executeUpdate();
		}
	}

	public Member findAccountById(long userId) throws Exception {
		Member member = new Member();
		Connection conn = dataSource.getConnection();

		String sql = queryMap.get("findByIdMember");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, userId);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				member.setId(rs.getInt("id"));
				member.setUsername(rs.getString("username"));
				member.setPassword(rs.getString("password"));
				member.setEmail(rs.getString("email"));
				member.setMenuType(rs.getString("menuType"));
				member.setSportType(rs.getString("sportType"));
				member.setType(rs.getString("type"));
				member.setImg(rs.getString("img"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return member;
	}

	public Member selectByUsername(String username) throws Exception {
		Connection conn = dataSource.getConnection();
		Member member = new Member();
		String sql = queryMap.get("selectByUsernameMember");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, username);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				member.setId(rs.getInt("id"));
				member.setUsername(rs.getString("username"));
				member.setPassword(rs.getString("password"));
				member.setEmail(rs.getString("email"));
				member.setMenuType(rs.getString("menuType"));
				member.setSportType(rs.getString("sportType"));
				member.setType(rs.getString("type"));
				member.setImg(rs.getString("img"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return member;
	}
	
	//마이페이지 사용
	public Object[] selectObjectByUsername(String username) throws Exception {
		Object[] c = null;

		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<Object[]> h = new ArrayHandler();
			Object[] p = {username};
			c = qr.query(queryMap.get("selectObjectByUsername"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return c;
	}

	public void addMember(Member m) throws Exception {
		Connection conn = dataSource.getConnection();
		String sql = queryMap.get("insertMember");
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, m.getUsername());
			pstmt.setString(2, m.getPassword());
			pstmt.setString(3, m.getEmail());
			pstmt.setString(4, m.getMenuType());
			pstmt.setString(5, m.getSportType());
			pstmt.setString(6, m.getType());
			pstmt.setString(7, null);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//아이디 중복 체크
	public boolean checkByUserName(String userName) throws Exception {
		Connection conn = dataSource.getConnection();

		String sql = queryMap.get("selectByUsernameMember");
		PreparedStatement pstmt = null;
		boolean rtn = false;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) { //중복 있음. 불가능
				rtn = false;
				
			} else { // 가능
				rtn = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	//이메일 중복 체크
	public boolean checkEmail(String email) throws Exception {
		Connection conn = dataSource.getConnection();

		String sql = queryMap.get("selectByEmailMember");
		PreparedStatement pstmt = null;
		boolean rtn = false;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) { //결과 있으면 불가능 (이미 존재하는 계정)
				rtn = false; 
				
			} else { //가능 (미존재 계정)
				rtn = true; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}

	public void updateSportType(Member m) throws Exception {
		Connection conn = dataSource.getConnection();

		String sql = queryMap.get("updateSportTypeMember");
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, m.getSportType());
			pstmt.setString(2, m.getUsername());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateMenuType(Member m) throws Exception {

		Connection conn = dataSource.getConnection();
		String sql = queryMap.get("updateMenuTypeMember");
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, m.getMenuType());
			pstmt.setString(2, m.getUsername());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int updatePwdByUserName(Member member) throws Exception {
		Connection conn = dataSource.getConnection();
		String sql = queryMap.get("updatePwdByUserNameMember");
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, member.getPassword());
			pstmt.setString(2, member.getUsername());
			
			int ret = pstmt.executeUpdate();
			if(ret > 0) {
				return 1;
			}else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Member selectByEmail(String email) throws Exception {
		Connection conn = dataSource.getConnection();
		String sql = queryMap.get("selectByEmailMember");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, email);
		ResultSet rs = pstmt.executeQuery();
		Member member = new Member();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				member.setId(rs.getInt("id"));
				member.setUsername(rs.getString("username"));
				member.setPassword(rs.getString("password"));
				member.setEmail(rs.getString("email"));
				member.setMenuType(rs.getString("menuType"));
				member.setSportType(rs.getString("sportType"));
				member.setType(rs.getString("type"));
				member.setImg(rs.getString("img"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return member;
	}
	
	public Member selectByUserNameAndEmail(String userName, String email) throws Exception {
		Connection conn = dataSource.getConnection();
		String sql = queryMap.get("selectByUserNameAndEmailMember");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, userName);
		pstmt.setString(2, email);
		ResultSet rs = pstmt.executeQuery();
		Member member = new Member();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				member.setId(rs.getInt("id"));
				member.setUsername(rs.getString("username"));
				member.setPassword(rs.getString("password"));
				member.setEmail(rs.getString("email"));
				member.setMenuType(rs.getString("menuType"));
				member.setSportType(rs.getString("sportType"));
				member.setType(rs.getString("type"));
				member.setImg(rs.getString("img"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return member;
	}
	
	public String selectEmailById(long userId) throws Exception {
		Connection conn = dataSource.getConnection();
		String sql = queryMap.get("selectEmailById");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, userId);
		ResultSet rs = pstmt.executeQuery();
		String email = "";
		
		try (conn; pstmt; rs) {
			while (rs.next()) {
				email = rs.getString("email");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return email;
	}

}
