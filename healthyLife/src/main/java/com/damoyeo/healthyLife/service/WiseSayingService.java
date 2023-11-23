package com.damoyeo.healthyLife.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.WiseSaying;
import com.damoyeo.healthyLife.dao.WiseSayingDAO;


@Service
public class WiseSayingService {
	@Autowired
	WiseSayingDAO wisesayingdao;

	public WiseSayingService() {
		wisesayingdao = new WiseSayingDAO();
	}
	
	//명언 관련 메소드
	public List<WiseSaying> getAllWiseSaying() { 
		List<WiseSaying> m = new ArrayList<>();
		m = wisesayingdao.select();
		return m;
	}
}
