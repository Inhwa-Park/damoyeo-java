package com.damoyeo.healthyLife.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.Menu;
import com.damoyeo.healthyLife.dao.MenuTestDAO;


@Service
public class MenuTestService {
	@Autowired
	MenuTestDAO menudao;
	
	public MenuTestService() {
		menudao = new MenuTestDAO();
	}
	
	public Menu find(String title) {		
		return menudao.selectByTitle(title);
	}

}
