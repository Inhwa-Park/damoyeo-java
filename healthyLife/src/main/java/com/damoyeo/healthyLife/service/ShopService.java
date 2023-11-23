package com.damoyeo.healthyLife.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.Cart;
import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.bean.Ordering;
import com.damoyeo.healthyLife.bean.Product;
import com.damoyeo.healthyLife.dao.CartDAO;
import com.damoyeo.healthyLife.dao.OrderingDAO;
import com.damoyeo.healthyLife.dao.ProductDAO;

@Service
public class ShopService {

	@Autowired
	ProductDAO productdao;
	
	@Autowired
	CartDAO cartdao;
	
	@Autowired
	OrderingDAO orderingdao;
	
	public ShopService() {
		productdao = new ProductDAO();
		cartdao = new CartDAO();
		orderingdao = new OrderingDAO();
	}
	
	//상품 목록 가져오기
	public List<Product> findAllProduct() throws Exception{
		return productdao.select();
	}
	
	//할인 상품 목록 가져오기
	public List<Product> findAllSaleProduct() throws Exception{
		return productdao.selectIsSale();
	}
	
	//상품 하나 가져오기
	public Product findProduct(long id) throws Exception{
		return productdao.selectById(id);
	}
	
	//타입별 상품 목록 가져오기
	public List<Product> findAllByType(String type) throws Exception{
		return productdao.selectByType(type);
	}
	
	//할인 상품 중 타입별로 목록 가져오기
	public List<Product> findAllSaleByType(String type) throws Exception{
		return productdao.selectSaleByType(type);
	}
	
	//상품 장바구니에 넣기
	public void addCart(Cart c) {
		cartdao.insert(c);
	}
	
	//장바구니 목록 가져오기
	public List<Cart> findAllCart(long memberId) throws Exception{
		return cartdao.selectById(memberId);
	}
	
	//장바구니 수량 변경하기
	public void updateAmount(Cart c) {
		cartdao.updateAmount(c);
	}
	
	//장바구니 상품 삭제하기
	public void deleteById(long productId) {
		cartdao.deleteById(productId);
	}
	
	//장바구니 상품 전체 삭제하기
	public void deleteAll() {
		cartdao.deleteAll();
	}
	
	//ordering 테이블에 주문 넣기
	public int addOrdering(Ordering o) {
		int result = orderingdao.insert(o);
		if(result>0) {
			return 1;
		}else {
			return -1;
		}
	}
	
	//id로 해당 유저의 주문 목록 가져오기
	public List<Ordering> findAllOrdering(long memberId) throws Exception{
		return orderingdao.selectAllById(memberId);
	}
	
	//username으로 해당 유저 주문목록 가져오기
	public List<Ordering> findAllOrdering(String username) throws Exception{
		return orderingdao.selectAllByUsername(username);
	}
	
	//해당 유저의 기간에 따른 주문 목록 가져오기
	public List<Ordering> SelectByPeriod(Date startDate, Date endDate) throws Exception{
		return orderingdao.SelectByPeriod(startDate, endDate);
	}
	
	//주문 삭제
	public int deleteOrdering(String payment) throws Exception {
		int result = orderingdao.delete(payment);
		return result;
	}
}
