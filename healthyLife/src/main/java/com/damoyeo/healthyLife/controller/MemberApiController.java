package com.damoyeo.healthyLife.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.AdminUser;
import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.exception.MemberException;
import com.damoyeo.healthyLife.exception.NotUserException;
import com.damoyeo.healthyLife.mail.RegisterMail;
import com.damoyeo.healthyLife.security.TokenProvider;
import com.damoyeo.healthyLife.service.AdminService;
import com.damoyeo.healthyLife.service.MemberService;

@RestController
@RequestMapping("/memberApi")
public class MemberApiController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private RegisterMail registerMail;
	@Autowired
	private TokenProvider tokenProvider;
	@Autowired
	private AdminService adminService;

	//회원 가입
	@PostMapping("/signup")
	public ResponseEntity<Map <String, String>> doSignUp(@RequestBody Member member) throws IOException {
		Map <String, String> codeMap = new HashMap<>();
		
		try {
			memberService.addOrRefuse(member, member.getType());
			member.setMenuType(null);
			member.setSportType(null);
			codeMap.put("data", "회원가입이 완료되었습니다.");
			
		} catch (MemberException e) {
			codeMap.put("error", "Signup post failed.");
			e.printStackTrace();
		}
		
		return ResponseEntity.ok().body(codeMap);
	}
	
	//회원가입 시 이메일 중복 여부 확인
	@GetMapping("/signup/checkEmail/{email}")
	public ResponseEntity<Map <String, String>> checkEmail(@PathVariable String email) throws Exception {
		boolean checked = memberService.CheckEmailExistence(email);
		Map <String, String> codeMap = new HashMap<>();
		
		if(checked == true) {
			codeMap.put("data", "회원가입 가능한 계정입니다.");
		}
		codeMap.put("error", "이미 가입된 계정입니다!");
		
		return ResponseEntity.ok().body(codeMap);
	}
	
	//회원가입 시 유저네임 중복 여부 확인
	@GetMapping("/signup/checkUserName/{username}")
	public ResponseEntity<Map <String, String>> checkUserName(@PathVariable String username) throws Exception {
		boolean checked = memberService.checkUserNameExistence(username);
		Map <String, String> codeMap = new HashMap<>();
		
		if(checked == true) {
			codeMap.put("data", "사용 가능한 아이디입니다.");
		}
		codeMap.put("error", "이미 사용 중인 아이디입니다! 다시 입력해주세요.");
		
		return ResponseEntity.ok().body(codeMap);
	}

	//로그인 토큰 생성
	@PostMapping("/signin") 
	public ResponseEntity<?> signin(@RequestBody Member userDTO) throws Exception {
		Member user = null;
		String token = null;
		AdminUser admin = null;
		String username = userDTO.getUsername();

		try {
			if(username.equals("관리자") != true) { //일반 사용자
				user = memberService.getByCredentials(username, userDTO.getPassword());
				token = tokenProvider.create(user);
				Member responseUser = Member.builder()
							.username(user.getUsername())
							.type(user.getType())
							.id(user.getId())
							.role(user.getRole())
							.token(token)
							.build();
					return ResponseEntity.ok().body(responseUser);
					
			}else {
				admin = adminService.getByCredentials(userDTO.getUsername(), userDTO.getPassword());
				token = tokenProvider.createAdmin(admin);
				AdminUser responseAdmin = AdminUser.builder()
						.adminName(userDTO.getUsername())
						.id(userDTO.getUsername())
						.role(admin.getRole())
						.token(token)
						.type("관리자")
						.build();
				return ResponseEntity.ok().body(responseAdmin);
			}
			
		} catch (NotUserException ne) {
			String msg = ne.getMessage();
			ResponseDTO responseDTO = ResponseDTO.builder().error(msg).build();
			return ResponseEntity.badRequest().body(responseDTO);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().body("???");
	}

	//이메일 인증
	@GetMapping("/mailConfirm/{email}")
	public ResponseEntity<Map<String, String>> mailConfirm(@PathVariable String email) throws Exception {

		String code = registerMail.sendSimpleMessage(email);
		System.out.println("인증코드 : " + code);
		Map<String, String> codeMap = new HashMap<>();
		codeMap.put("code", code);
		return ResponseEntity.ok().body(codeMap);
	}

	//아이디 찾기
	@GetMapping("/findId/{email}") 
	public ResponseEntity<?> findId(@PathVariable String email) throws Exception {

		try {
			String username = memberService.findId(email);
			if (username != null) {
				Member responseUser = Member.builder().username(username).build();
				return ResponseEntity.ok().body(responseUser);
			}
		} catch (MemberException e) {
			e.printStackTrace();
		}
		ResponseDTO responseDTO = ResponseDTO.builder().error("가입된 이력이 없는 이메일입니다.").build();
		return ResponseEntity.badRequest().body(responseDTO);
	}

	//비밀번호 찾기
	@PostMapping("/findPwd") 
	public ResponseEntity<?> findPwd(@RequestBody Member userDTO) throws Exception {
		Member user = null;

		try {
			user = memberService.getMemberExistence(userDTO.getUsername(), userDTO.getEmail());

		} catch (NotUserException ne) {
			ResponseDTO responseDTO = ResponseDTO.builder().error(ne.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Member responseUser = Member.builder().username(user.getUsername()).id(user.getId()).email(user.getEmail())
				.build();
		return ResponseEntity.ok().body(responseUser);
	}

	//비번찾기 시 비밀번호 수정
	@PostMapping("/pwdUpdate")
	public ResponseEntity<?> pwdUpdate(@RequestBody Map<String, String> param)
			throws Exception {
		Map<String, String> editMap = new HashMap<>();
		try {
			Member m = memberService.findAccount(memberService.findIdByUserName(param.get("userName")));
			m.setPassword(param.get("password"));
			int result = memberService.editPassword(m);
			if (result > 0) { // 성공
				editMap.put("data", "비밀번호 변경이 완료 되었습니다.");
			}
			return ResponseEntity.ok().body(editMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseDTO responseDTO = ResponseDTO.builder().error("Password update failed.").build();
		return ResponseEntity.badRequest().body(responseDTO);
	}
	
	//로그인 후 비밀번호 수정
	@PostMapping("/pwdEdit") 
	public ResponseEntity<?> pwdEdit(
			@RequestBody Map<String, String> passwordMap, @AuthenticationPrincipal GeneralUser generalUser) throws Exception {
		
		Map <String, String> editMap = new HashMap<>();
		try {
			Member m = memberService.findAccount(generalUser.getId());
			m.setPassword(passwordMap.get("password"));
			int daoSuccess = memberService.editPassword(m);
			if(daoSuccess > 0) {//성공한 경우
				editMap.put("data", "비밀번호 변경이 완료 되었습니다.");
			}
			return ResponseEntity.ok().body(editMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseDTO responseDTO = ResponseDTO.builder().error("faild").build();
		return ResponseEntity.badRequest().body(responseDTO);
	}
	
	//이메일 찾기
	@GetMapping("/findEmail/{userId}") 
	public ResponseEntity<?> findEmail(@PathVariable long userId) throws Exception {
		Map<String, String> editMap = new HashMap<>();
		String email = memberService.findEmail(userId);
		editMap.put("email", email);
		return ResponseEntity.ok().body(editMap);
	}
}
