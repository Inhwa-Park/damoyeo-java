package com.damoyeo.healthyLife.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.Calorie;
import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Product;
import com.damoyeo.healthyLife.service.CalorieService;
import com.damoyeo.healthyLife.service.CommunityService;
import com.damoyeo.healthyLife.service.ShopService;

@RestController
@RequestMapping("/homeApi")
public class HomeApiController {
	
	@Autowired
	private CommunityService communityService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private CalorieService calorieService;
	
	//조회수 높은 순으로 게시물 두 개만
	@GetMapping("/getPosts") 
	public ResponseEntity<?>  getPosts() throws Exception {
		List<Community> viewList = communityService.getPostByViews();
		return ResponseEntity.ok().body(viewList);
	}
	
	//할인 상품 중 랜덤 두 개만
	@GetMapping("/getProducts") 
	public ResponseEntity<?>  getProducts() throws Exception {
		List<Product> viewList = shopService.findAllSaleProduct();
		List<Product> rtnList = new ArrayList<>();
		
		Random r = new Random();
		for(int i=0; i<2; i++) {
			int index = r.nextInt(viewList.size());
			rtnList.add(viewList.get(index));
			viewList.remove(index);
		}
		
		return ResponseEntity.ok().body(rtnList);
	}
	
	//칼로리 사전 중 랜덤 세 개만
	@GetMapping("/getCalories") 
	public ResponseEntity<?>  getCalories() throws Exception {
		List<Calorie> viewList = calorieService.findAll();
		List<Calorie> rtnList = new ArrayList<>();
		
		Random r = new Random();
		for(int i=0; i<3; i++) {
			int index = r.nextInt(viewList.size());
			rtnList.add(viewList.get(index));
			viewList.remove(index);
		}
		return ResponseEntity.ok().body(rtnList);
	}
	
	public static Long castLong(Object o) {
        Long value = null;
        if (o != null) {
            value = Long.parseLong(o.toString());
        }
        return value;
    }
	
	public static String castString(Object o) {
        String value = null;
        if (o != null) {
            value = o.toString();
        }
        return value;
    }

}
