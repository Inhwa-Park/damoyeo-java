package com.damoyeo.healthyLife.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.service.CommentService;
import com.damoyeo.healthyLife.service.MemberService;

@RestController
@RequestMapping("/commentApi")
public class CommentApiController {
	@Autowired
	private CommentService commentService;
	@Autowired
	private MemberService memberService;

	@GetMapping("/getAll/{listId}") 
	public ResponseEntity<?>  authenticate(@AuthenticationPrincipal GeneralUser generalUser, @PathVariable long listId) throws Exception{

		List<Comment> viewList = new ArrayList<>();
		List<Object[]> allCommentList= commentService.getComment(listId);
			
		for(Object[] o : allCommentList) {
			Comment c = new Comment();
			c.setId(castLong(o[0]));
			c.setMemberId(castLong(o[1]));
			c.setUsername(castString(o[2]));
			c.setMemberType(castString(o[3]));
			c.setCommentDate((java.sql.Date)(o[4]));
			c.setContent(castString(o[5]));
			c.setCommunityId(castLong(o[6]));
			viewList.add(c);			
		}
		return ResponseEntity.ok().body(viewList);
	}

	// 댓글추가
	@PostMapping("/addComment")
	public ResponseEntity<?> addComment(@AuthenticationPrincipal GeneralUser generalUser, @RequestBody Comment comment) throws Exception {

		// 리스트 아이디=커뮤니티.커뮤니티 아이디, 코멘트텍스트=커뮤니티.컨텐트
		long memberId = castLong(generalUser.getId());
		commentService.addComment(comment.getCommunityId(), memberId, comment.getContent());
		commentService.addViewsAtCommunity(comment.getCommunityId());
		
		//추가한 댓글 포함한 댓글 리스트 리턴해주는 걸로 수정함. 1024 인화
		List<Comment> viewList = new ArrayList<>();
		List<Object[]> allCommentList= commentService.getComment(comment.getCommunityId());
			
		for(Object[] o : allCommentList) {
			Comment c = new Comment();
			c.setId(castLong(o[0]));
			c.setMemberId(castLong(o[1]));
			c.setUsername(castString(o[2]));
			c.setMemberType(castString(o[3]));
			c.setCommentDate((java.sql.Date)(o[4]));
			c.setContent(castString(o[5]));
			c.setCommunityId(castLong(o[6]));
			viewList.add(c);			
		}
		return ResponseEntity.ok().body(viewList);
	}
	
	//댓글 삭제
	@PostMapping("/deleteComment")
	public ResponseEntity<?> deleteComment(@AuthenticationPrincipal GeneralUser generalUser, @RequestBody Map<String, Long> param) throws Exception {
		//맵 안에 코멘트아이디, 리스트아이디 넣어서 requestBody로 받는 걸로 수정함. 1024 인화
		//파람.코멘트 아이디(삭제할 댓글 아이디), 파람.포스트 아이디(해당 게시물 아이디)
		
		commentService.deleteComment(param.get("commentId"));
		commentService.addViewsAtCommunity(param.get("postId"));
		
		List<Comment> viewList = new ArrayList<>();
		List<Object[]> allCommentList= commentService.getComment(param.get("postId"));
			
		for(Object[] o : allCommentList) {
			Comment c = new Comment();
			c.setId(castLong(o[0]));
			c.setMemberId(castLong(o[1]));
			c.setUsername(castString(o[2]));
			c.setMemberType(castString(o[3]));
			c.setCommentDate((java.sql.Date)(o[4]));
			c.setContent(castString(o[5]));
			c.setCommunityId(castLong(o[6]));
			viewList.add(c);			
		}
		return ResponseEntity.ok().body(viewList);
	}

	public static Long castLong(Object o) {
		Long value = null;
		if (o != null) {
			value = Long.parseLong(o.toString());
		}
		return value;
	}

	public static String castString(Object o) {
		String value = null;
		if (o != null) {
			value = o.toString();
		}
		return value;
	}

	public static Date castDate(Object o) throws Exception {
		Date value = null;
		if (o != null) {
			String from = o.toString();
			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			value = fm.parse(from);
		}
		return value;
	}
}
