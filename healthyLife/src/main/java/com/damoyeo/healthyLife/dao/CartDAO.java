package com.damoyeo.healthyLife.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Cart;
import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.bean.Product;

@Component
public class CartDAO {
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
	
	public CartDAO() {
		super();
	}
	
	public void insert(Cart c) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			ScalarHandler<Long> h = new ScalarHandler<>();
			Object[] p = {c.getProductId(), c.getMemberId(), c.getAmount(), c.getSalePrice()};
			long num = qr.insert(queryMap.get("addCart"), h, p);
			c.setId(num); 
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public List<Cart> selectById(long memberId) throws Exception {
		Connection conn = dataSource.getConnection();
		List<Cart> carts = new ArrayList<>();

		String sql = queryMap.get("selectByUser");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, memberId);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				Cart cart = new Cart();
				cart.setId(rs.getLong("id"));
				cart.setProductId(rs.getLong("productId"));
				cart.setMemberId(rs.getLong("memberId"));
				cart.setAmount(rs.getLong("amount"));
				cart.setSalePrice(rs.getLong("salePrice"));
				cart.setProductName(rs.getString("name"));
				carts.add(cart);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return carts;
	}
	
	public void updateAmount(Cart c) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			Object[] p = {c.getAmount(), c.getId()};
			qr.execute(queryMap.get("updateAmount"), p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public void deleteById(long productId) {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			Object[] p = {productId};
			qr.execute(queryMap.get("deleteById"),p);
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	public void deleteAll() {
		try {
			QueryRunner qr = new QueryRunner(dataSource);
			qr.execute(queryMap.get("deleteAll"));
			
		}catch(SQLException se) {
			se.printStackTrace();
		}
	}
}
