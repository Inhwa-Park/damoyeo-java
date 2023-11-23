package com.damoyeo.healthyLife.service;

import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.Calorie;
import com.damoyeo.healthyLife.dao.CalorieDAO;

@Service
public class CalorieService {
	@Autowired
	CalorieDAO caloriedao;
	
	public CalorieService() {
		caloriedao = new CalorieDAO();
	}
	
	public List<Calorie> findAll() {		
		return caloriedao.select();
	}
	
	public List<Calorie> findByTitle(String title) {
		return caloriedao.selectByTitle(title);
	}
	
	public List<Calorie> findAllByType(String type) {
		return caloriedao.selectByType(type);
	}
}
