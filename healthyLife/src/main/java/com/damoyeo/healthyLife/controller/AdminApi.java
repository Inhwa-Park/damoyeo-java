package com.damoyeo.healthyLife.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.Comment;
import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.Ordering;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.exception.MemberException;
import com.damoyeo.healthyLife.service.AdminService;
import com.damoyeo.healthyLife.service.CommentService;
import com.damoyeo.healthyLife.service.CommunityService;
import com.damoyeo.healthyLife.service.MemberService;
import com.damoyeo.healthyLife.service.ShopService;

@RestController
@RequestMapping("/adminApi")
public class AdminApi {
	@Autowired
	private AdminService adminService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ShopService shopService;

	// 회원아이디로 탈퇴
	@PostMapping("/resignUser")
	public ResponseEntity<ResponseDTO> resignUser(@RequestParam("userId") Long userIdParam) throws Exception {
		try {
			memberService.resignAccount(userIdParam);

		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder().error("faild").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
		ResponseDTO responseDTO = ResponseDTO.builder().msg("해당 회원이 탈퇴 되었습니다.").build();
		return ResponseEntity.ok().body(responseDTO);
	}

	// 회원이름으로 조회
	@GetMapping("/findUser/{usernameParam}")
	public ResponseEntity<ResponseDTO<Member>> findUser(@AuthenticationPrincipal GeneralUser generalUser,
			@PathVariable String usernameParam) throws Exception {

		List<Member> viewList = new ArrayList<>();

		try {
			Member m = memberService.findIdByUsername(usernameParam);
			viewList.add(m);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder().error("faild").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
		ResponseDTO<Member> responseDTO = ResponseDTO.<Member>builder().data(viewList).build();
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/admin")
	public String admin() { // test코드
		return "admin";
	}

	// 관리자가 게시판 회원 조회할 때 매서드
	@GetMapping("/findPostByuser/{usernameParam}")
	public ResponseEntity<ResponseDTO<Community>> findPostByusername(@AuthenticationPrincipal GeneralUser generalUser,
			@PathVariable String usernameParam) {

		List<Community> viewList = new ArrayList<>();
		try {
			viewList = communityService.usernameFind(usernameParam);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder().error("admin couldn't load fail").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
		ResponseDTO<Community> responseDTO = ResponseDTO.<Community>builder().data(viewList).build();
		return ResponseEntity.ok().body(responseDTO);
	}

	// 관리자가 댓글 회원 조회할 때 매서드
	@GetMapping("/findCommentByuser/{usernameParam}")
	public ResponseEntity<ResponseDTO<Comment>> findCommentByuser(@AuthenticationPrincipal GeneralUser generalUser,
			@PathVariable String usernameParam) {

		List<Comment> viewList = new ArrayList<>();
		try {
			long userId = memberService.findIdByUsername(usernameParam).getId();
			viewList = commentService.findByUsername(userId);

		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder().error("admin couldn't load fail").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
		ResponseDTO<Comment> responseDTO = ResponseDTO.<Comment>builder().data(viewList).build();
		return ResponseEntity.ok().body(responseDTO);
	}

	// 회원 목록 전체 가져오기
	@GetMapping("/findAllUser")
	public ResponseEntity<List<Member>> findAllUser(@AuthenticationPrincipal GeneralUser generalUser) throws Exception {
		List<Member> viewList = memberService.findAllUser();
		return ResponseEntity.ok().body(viewList);
	}

	// 타입별 회원 목록 전체 가져오기
	@GetMapping("/findAllUserByType/{type}")
	public ResponseEntity<List<Member>> findAllUserByType(@AuthenticationPrincipal GeneralUser generalUser,
			@PathVariable String type) throws Exception {
		List<Member> viewList = memberService.findAllUserByType(type);
		return ResponseEntity.ok().body(viewList);
	}

	// 유저 이름으로 주문내역 조회
	@GetMapping("/findOrderingByuser/{usernameParam}")
	public ResponseEntity<?> findOrderingByuser(@AuthenticationPrincipal GeneralUser generalUser,
			@PathVariable String usernameParam) throws Exception {

		List<Ordering> viewList = new ArrayList<>();
		viewList = shopService.findAllOrdering(usernameParam);
		return ResponseEntity.ok().body(viewList);
	}

	// 강퇴
	@DeleteMapping("/resign/{userId}")
	public ResponseEntity<ResponseDTO> resign(@AuthenticationPrincipal String id, @PathVariable String userId)
			throws Exception {

		try {
			memberService.resignAccount(Long.parseLong(userId));
			ResponseDTO responseDTO = ResponseDTO.builder().msg("탈퇴 처리 되었습니다.").build();
			return ResponseEntity.ok().body(responseDTO);
		} catch (MemberException e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder().error("faild").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	// 댓글 삭제
	@PostMapping("/deleteComment")
	public ResponseEntity<?> deleteComment(@AuthenticationPrincipal GeneralUser generalUser,
			@RequestBody Map<String, Long> param) throws Exception {
		// 맵 안에 코멘트아이디, 리스트아이디 넣어서 requestBody로 받는 걸로 수정함. 1024 인화
		// param.commentId(삭제할 댓글 아이디), param.postId(해당 게시물 아이디)

		commentService.deleteComment(param.get("commentId"));
		List<Comment> viewList = new ArrayList<>();
		viewList = commentService.findByUsername(param.get("memberId"));

		return ResponseEntity.ok().body(viewList);
	}

	// 게시물 삭제
	@GetMapping("/deletePost/{listId}")
	public ResponseEntity<?> deletePost(@AuthenticationPrincipal GeneralUser gerneralUser,
			@PathVariable("listId") long listIdParam) {

		try {
			Object[] c = communityService.deletePost(listIdParam);

			MultipartConfig mc = getClass().getAnnotation(MultipartConfig.class);

			if (mc!= null && c[2] != null) {
				String n = c[2].toString();
				n = n.substring(1, n.length() - 1);

				String fileName = n;
				String[] names = fileName.split(";");

				for (String name : names) {
					File file = new File(mc.location() + name);
					System.out.println("file: " + file);
					file.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseDTO responseDTO = ResponseDTO.builder().error("게시물 삭제 시 오류 발생!").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
		ResponseDTO responseDTO = ResponseDTO.builder().msg("게시물이 정상적으로 삭제되었습니다.").build();
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//주문내역 삭제
	@GetMapping("/deleteOrdering/{payment}")
	public ResponseEntity<?> deleteOrdering(@AuthenticationPrincipal GeneralUser generalUser,
			@PathVariable String payment) throws Exception {

		int result = shopService.deleteOrdering(payment);
		
		if(result > 0) {
			//삭제 성공
			ResponseDTO responseDTO = ResponseDTO.builder().msg("주문 내역이 정상적으로 삭제되었습니다.").build();
			return ResponseEntity.ok().body(responseDTO);
		}else {
			//삭제 실패
			ResponseDTO responseDTO = ResponseDTO.builder().msg("주문 내역 삭제 중 오류 발생!").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}


}
