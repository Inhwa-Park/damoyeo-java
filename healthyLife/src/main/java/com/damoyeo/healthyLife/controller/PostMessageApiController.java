package com.damoyeo.healthyLife.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.PostMessage;
import com.damoyeo.healthyLife.bean.Schedule;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.dto.WeeklyDTO;
import com.damoyeo.healthyLife.service.MemberService;
import com.damoyeo.healthyLife.service.PostMessageService;
import com.damoyeo.healthyLife.service.TrainerService;

@RestController
@RequestMapping("/postMessageApi")
public class PostMessageApiController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private PostMessageService postMsgService;
	@Autowired
	private TrainerService trainerService;
	
	@PostMapping("/sendMessage") //유저가 트레이너에게 쪽지 보낼때
	public ResponseEntity<ResponseDTO> SendMessage(@AuthenticationPrincipal GeneralUser gerneralUser,
			@RequestBody PostMessage pmg)throws Exception {
		Member m = memberService.findAccount(gerneralUser.getId());
		System.out.println(pmg);
		if(m != null) {
			pmg.setMemberId(gerneralUser.getId());
			pmg.setTrainerId(memberService.findIdByUsername(pmg.getRecieveName()).getId());
			postMsgService.addMessage(pmg);
					
			ResponseDTO responseDTO = ResponseDTO.builder().msg("쪽지가 정상적으로 전송 되었습니다.").build();
			return ResponseEntity.ok().body(responseDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("postMessage loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@GetMapping("/getAllByUserId") //처음 페이지 입장할때 회원/트레이너에 따른 Role String 부여
	public ResponseEntity<ResponseDTO<PostMessage>> getAllByUserId(@AuthenticationPrincipal GeneralUser gerneralUser) throws Exception{
		Member m = memberService.findAccount(gerneralUser.getId());
		System.out.println(gerneralUser.getId());;
		if(m != null) {
			List<PostMessage> viewList = postMsgService.getAllMessagesByUserId(gerneralUser.getId());
					
			ResponseDTO<PostMessage> responseDTO = ResponseDTO.<PostMessage>builder().data(viewList).build();
			return ResponseEntity.ok().body(responseDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("postMessage loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@GetMapping("/editPmg/{postMsgId}") //수정 아이콘 누르고 해당 정보 반환
	public ResponseEntity<ResponseDTO<PostMessage>> editPmgClick(@AuthenticationPrincipal GeneralUser gerneralUser,
			@PathVariable String postMsgId) throws Exception{
		Member m = memberService.findAccount(gerneralUser.getId());
		if(m != null) {
			List<PostMessage> viewList = postMsgService.getAllMessagesByPsgId(Long.parseLong(postMsgId));		
			ResponseDTO<PostMessage> responseDTO = ResponseDTO.<PostMessage>builder().data(viewList).build();
			return ResponseEntity.ok().body(responseDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("postMessage loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/doEdit") //수정하기 버튼 누를때
	public ResponseEntity<ResponseDTO<PostMessage>> editPmgSend(
			@AuthenticationPrincipal GeneralUser gerneralUser, @RequestBody PostMessage pmg) throws Exception{
		 
		Member m = memberService.findAccount(gerneralUser.getId());
		pmg.setMemberId(memberService.findIdByUsername(pmg.getSenderName()).getId());
		pmg.setTrainerId(memberService.findIdByUsername(pmg.getRecieveName()).getId());

		if(m != null) {
			List<PostMessage> viewList = postMsgService.editMessage(pmg);	
			ResponseDTO<PostMessage> responseDTO = ResponseDTO.<PostMessage>builder().data(viewList).build();
			return ResponseEntity.ok().body(responseDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("postMessage loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@GetMapping("/getPmg/{postMsgId}") //트레이너가 답변버튼을 누를때 해당 질문의 정보를 반환.
	public ResponseEntity<ResponseDTO<PostMessage>> getPmg(@AuthenticationPrincipal GeneralUser gerneralUser,
			@PathVariable String postMsgId) throws Exception{
		Member m = memberService.findAccount(gerneralUser.getId());
		if(m != null) {
			List<PostMessage> viewList = postMsgService.getAllMessagesByPsgId(Long.parseLong(postMsgId));		
			ResponseDTO<PostMessage> responseDTO = ResponseDTO.<PostMessage>builder().data(viewList).build();
			return ResponseEntity.ok().body(responseDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("postMessage loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@DeleteMapping("/deletePmg/{postMsgId}")
	public ResponseEntity<ResponseDTO<PostMessage>> SendMessage(@AuthenticationPrincipal GeneralUser gerneralUser,
			@PathVariable String postMsgId)throws Exception {
		Member m = memberService.findAccount(gerneralUser.getId());
		if(m != null) {
			postMsgService.deleteMessage(Long.parseLong(postMsgId));
			List<PostMessage> viewList = postMsgService.getAllMessagesByUserId(gerneralUser.getId());
			ResponseDTO<PostMessage> responseDTO = ResponseDTO.<PostMessage>builder().msg("쪽지가 정상적으로 삭제 되었습니다.").data(viewList).build();
			return ResponseEntity.ok().body(responseDTO);
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder().error("postMessage loading failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
