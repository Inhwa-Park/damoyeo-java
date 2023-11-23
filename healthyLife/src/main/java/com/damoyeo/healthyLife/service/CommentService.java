package com.damoyeo.healthyLife.service;
	
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.damoyeo.healthyLife.bean.Comment;
import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.dao.CommentDAO;

@Service
public class CommentService {
	@Autowired
	CommentDAO commentdao;
	
	public CommentService() {
		commentdao = new CommentDAO();
	}
	
	//댓글 추가
	public void addComment(long listId, long memberId, String context) {
		commentdao.insert(listId, memberId, context);
	}
	
	//댓글수 업데이트 기능
	public void addViewsAtCommunity(long listId) {
		commentdao.updateViews(listId);
	}
	
	public List<Object[]> getComment(long listId) {
		return commentdao.select(listId);
	}
	
	//댓글 삭제
	public void deleteComment(long listId) {
		commentdao.delete(listId);
	}
	
	public List<Comment> findByUsername(long memberId){
		List<Comment> rtn = new ArrayList<>();
		List<Object[]> allList = null;
		try{
			allList = commentdao.selectByMemberId(memberId);
			if(allList == null) { 
				System.out.println("memberId 조회 불가능, 유저가 작성한 댓글이 없습니다.");
				return rtn;
			}
			for(Object[] o : allList) {
				Comment c = new Comment();
				c.setId(castLong(o[0]));
				c.setMemberId(castLong(o[1]));
				c.setContent(castString(o[2]));
				c.setCommunityId(castLong(o[3]));
				c.setCommentDate(castDate(o[4]));
				rtn.add(c);			
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rtn;
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
