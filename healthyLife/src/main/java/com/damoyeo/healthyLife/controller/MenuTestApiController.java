package com.damoyeo.healthyLife.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.Menu;
import com.damoyeo.healthyLife.bean.Sport;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.service.MemberService;
import com.damoyeo.healthyLife.service.MenuTestService;

@RestController
@RequestMapping("/menuApi")
public class MenuTestApiController {

	@Autowired
	private MenuTestService menuService;
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/menuTestResult/{menuTypeTitle}") //토큰달고 오는 테스트코드
	public ResponseEntity<?>  authenticate(@AuthenticationPrincipal GeneralUser generalUser, @PathVariable String menuTypeTitle ) throws Exception{
		
		//1. 유저 토큰, sportType이 파라미터 날라옴. 토큰에 해당하는 아이디를 조회하여 고객을 추출, 그 고객의 스포츠 타입을 삽입, 고객객체를 디비에 저장. sportType정보 추출후 출력
		Member m = memberService.findAccount(generalUser.getId());
		if(m != null) {
			m.setMenuType(menuTypeTitle);
			memberService.setMenuType(m);
			Menu menu = menuService.find(menuTypeTitle);
			final Menu response = Menu.builder()
					.title(menu.getTitle())
					.content(menu.getContent())
					.subContent(menu.getSubContent())
					.nickname(menu.getNickname())
					.build();
			return ResponseEntity.ok().body(response);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("menuTestResult loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
