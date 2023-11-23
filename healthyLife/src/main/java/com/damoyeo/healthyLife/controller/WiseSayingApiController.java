package com.damoyeo.healthyLife.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.WiseSaying;
import com.damoyeo.healthyLife.service.WiseSayingService;


@RestController
@RequestMapping("/wiseSayingApi")
public class WiseSayingApiController {
	@Autowired
	private WiseSayingService wiseSayingService;
	
	@GetMapping
	public ResponseEntity<List<WiseSaying>> getAllWiseSaying() {
		List<WiseSaying> wiseSayingList = new ArrayList<>();
		List<WiseSaying> viewList = new ArrayList<>();
		wiseSayingList = wiseSayingService.getAllWiseSaying();
		for(WiseSaying w : wiseSayingList) {
			viewList.add(w);
		}
		return ResponseEntity.ok().body(viewList);
	}
}
