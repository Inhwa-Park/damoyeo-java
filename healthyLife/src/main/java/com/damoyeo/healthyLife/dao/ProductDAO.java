package com.damoyeo.healthyLife.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.damoyeo.healthyLife.bean.Product;

@Component
public class ProductDAO {
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
	
	public ProductDAO() {
		super();
	}

   public List<Product> select() throws Exception {
		Connection conn = dataSource.getConnection();
		List<Product> products = new ArrayList<>();

		String sql = queryMap.get("selectProduct");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				Product p = new Product();
				p.setId(rs.getLong("id"));
				p.setName(rs.getString("name"));
				p.setPrice(rs.getLong("price"));
				p.setSale(rs.getBoolean("isSale"));
				p.setDiscountPrice(rs.getLong("discountPrice"));
				p.setImage(rs.getString("image"));
				p.setDescription(rs.getString("description"));
				p.setType(rs.getString("type"));
				products.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}
   
   public Product selectById(long id) throws Exception {
	   Product product = new Product();
		Connection conn = dataSource.getConnection();

		String sql = queryMap.get("selectByIdProduct");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, id);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				product.setId(rs.getLong("id"));
				product.setName(rs.getString("name"));
				product.setPrice(rs.getLong("price"));
				product.setSale(rs.getBoolean("isSale"));
				product.setDiscountPrice(rs.getLong("discountPrice"));
				product.setImage(rs.getString("image"));
				product.setDescription(rs.getString("description"));
				product.setType(rs.getString("type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}
   
   public List<Product> selectByType(String type) throws Exception {
		Connection conn = dataSource.getConnection();
		List<Product> products = new ArrayList<>();

		String sql = queryMap.get("selectByTypeProduct");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, type);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getLong("id"));
				product.setName(rs.getString("name"));
				product.setPrice(rs.getLong("price"));
				product.setSale(rs.getBoolean("isSale"));
				product.setDiscountPrice(rs.getLong("discountPrice"));
				product.setImage(rs.getString("image"));
				product.setDescription(rs.getString("description"));
				product.setType(rs.getString("type"));
				products.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}
   
   public List<Product> selectIsSale() throws Exception {
		Connection conn = dataSource.getConnection();
		List<Product> products = new ArrayList<>();

		String sql = queryMap.get("selectIsSale");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				Product p = new Product();
				p.setId(rs.getLong("id"));
				p.setName(rs.getString("name"));
				p.setPrice(rs.getLong("price"));
				p.setSale(rs.getBoolean("isSale"));
				p.setDiscountPrice(rs.getLong("discountPrice"));
				p.setImage(rs.getString("image"));
				p.setDescription(rs.getString("description"));
				p.setType(rs.getString("type"));
				products.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}
   
   public List<Product> selectSaleByType(String type) throws Exception {
		Connection conn = dataSource.getConnection();
		List<Product> products = new ArrayList<>();

		String sql = queryMap.get("selectSaleByType");
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, type);
		ResultSet rs = pstmt.executeQuery();
		try (conn; pstmt; rs) {
			while (rs.next()) {
				Product product = new Product();
				product.setId(rs.getLong("id"));
				product.setName(rs.getString("name"));
				product.setPrice(rs.getLong("price"));
				product.setSale(rs.getBoolean("isSale"));
				product.setDiscountPrice(rs.getLong("discountPrice"));
				product.setImage(rs.getString("image"));
				product.setDescription(rs.getString("description"));
				product.setType(rs.getString("type"));
				products.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}
   
   
}
