package com.damoyeo.healthyLife.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.Calorie;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.service.CalorieService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calorieApi")
public class CalorieApiController {
	@Autowired
	private CalorieService calorieService;
	
	@GetMapping("/dictionary/{searchContent}") 
	public ResponseEntity<?>  authenticate(@PathVariable String searchContent) throws Exception{
	List<Calorie> searchList = new ArrayList<>();
	List<Calorie> viewList = new ArrayList<>();
	searchList = calorieService.findByTitle(searchContent);
	
	if(searchList != null){
		for(Calorie c : searchList) {
			Calorie unit = new Calorie();
			unit.setId(c.getId());
			unit.setType(c.getType());
			unit.setTitle(c.getTitle());
			unit.setCalory(c.getCalory());
			unit.setUnit(c.getUnit());
			viewList.add(unit);
		}
		return ResponseEntity.ok().body(viewList);
		
	}else {
		ResponseDTO responseDTO = ResponseDTO.builder().error("Calorie Dictionary searching failed.").build();
		return ResponseEntity.badRequest().body(responseDTO);
	}
	
	}
	
	@GetMapping("/dictionaryFind/{type}") 
	public ResponseEntity<List<Calorie>> getCalorieListByType(@PathVariable String type) throws Exception{
	
	List<Calorie> searchList = new ArrayList<>();
	List<Calorie> viewList = new ArrayList<>();
	
	searchList = calorieService.findAllByType(type);
	
	if(searchList != null){
		for(Calorie c : searchList) {
			Calorie unit = new Calorie();
			unit.setId(c.getId());
			unit.setType(c.getType());
			unit.setTitle(c.getTitle());
			unit.setCalory(c.getCalory());
			unit.setUnit(c.getUnit());
			unit.setMyType(c.getMyType());
			viewList.add(unit);
		}
		return ResponseEntity.ok().body(viewList);
		
	}else {
		ResponseDTO responseDTO = ResponseDTO.builder().error("Calorie Dictionary loading failed.").build();
		return ResponseEntity.badRequest().body(viewList);
	}
	
	}
}
