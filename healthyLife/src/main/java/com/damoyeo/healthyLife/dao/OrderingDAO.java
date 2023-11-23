package com.damoyeo.healthyLife.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Cart;
import com.damoyeo.healthyLife.bean.Ordering;

@Component
public class OrderingDAO {
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
	
	public OrderingDAO() {
		super();
	}
	
	public int insert(Ordering o) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ScalarHandler<Long> h = new ScalarHandler<>();
			Object[] p = {o.getProductId(), o.getMemberId(), o.getOrderDate(), o.getSalePrice(), 
					o.getAmount(), o.getPayment(), o.getAddress(), o.getPhone(), o.getUsername(), o.getZoneCode(), o.getName()};
			long num = qr.insert(queryMap.get("addOrdering"), h, p);
			o.setId(num); 
			return 1;
			
		}catch(SQLException se) {
			se.printStackTrace();
			return -1;
		}
	}
	
	public List<Ordering> selectAllById(long memberId) throws Exception {
		List<Ordering> rtn =  new ArrayList<>();
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Ordering>> h = new BeanListHandler<>(Ordering.class);
			Object[] p = {memberId};
			rtn = qr.query(queryMap.get("selectAllById"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return rtn;
	}
	
	public List<Ordering> selectAllByUsername(String username) throws Exception {
		Connection conn = dataSource.getConnection();
		List<Ordering> orders = new ArrayList<>();

		String sql = queryMap.get("selectAllByUsername");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, username);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				Ordering ordering = new Ordering();
				ordering.setId(rs.getLong("id"));
				ordering.setProductId(rs.getLong("productId"));
				ordering.setMemberId(rs.getLong("memberId"));
				ordering.setAmount(rs.getLong("amount"));
				ordering.setSalePrice(rs.getLong("salePrice"));
				ordering.setOrderDate(rs.getDate("orderDate"));
				ordering.setPayment(rs.getString("payment"));
				ordering.setAddress(rs.getString("address"));
				ordering.setPhone(rs.getString("phone"));
				ordering.setUsername(rs.getString("username"));
				ordering.setZoneCode(rs.getString("zoneCode"));
				ordering.setName(rs.getString("name"));
				orders.add(ordering);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}
	
	public List<Ordering> SelectByPeriod(Date startDate, Date endDate){
		List<Ordering> rtn =  new ArrayList<>();
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ResultSetHandler<List<Ordering>> h = new BeanListHandler<>(Ordering.class);
			Object[] p = {startDate, endDate};
			rtn = qr.query(queryMap.get("selectByPeriod"), h, p);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return rtn;
	}
	
	public int delete(String payment) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			Object[] p = {payment};
			qr.execute(queryMap.get("deleteOrdering"),p);
			return 1;
			
		}catch(SQLException se) {
			se.printStackTrace();
			return -1;
		}
	}
}
