package com.damoyeo.healthyLife.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.Sport;
import com.damoyeo.healthyLife.dao.SportTestDAO;

@Service
public class SportTestService {
	@Autowired
	SportTestDAO sportTestDao;
	
	public SportTestService() {
		sportTestDao = new SportTestDAO();
	}
	
	public Sport find(String title) {
		return sportTestDao.selectByTitle(title);
	}
	
}
