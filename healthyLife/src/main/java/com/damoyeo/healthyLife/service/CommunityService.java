package com.damoyeo.healthyLife.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.dao.CommunityDAO;

@Service
public class CommunityService {
	@Autowired
	CommunityDAO communitydao;

	public CommunityService() {
		communitydao = new CommunityDAO();
	}

	public List<Object[]> getAll(String searchType, String searchContent) {
		List<Object[]> rtn = null;
		if (searchType == null) {
			rtn = communitydao.select();
		} else {
			if (searchType.equals("title")) {
				rtn = communitydao.getSearchByContent(searchContent);
			}
			if (searchType.equals("writer")) {
				rtn = communitydao.getSearchByUserName(searchContent);
			}
			if (searchType.equals("date")) {
				rtn = communitydao.getSearchByUserDate(searchContent);
			}
		}
		return rtn;
	}

	public Object[] getPost(long listId) {
		return communitydao.selectById(listId);
	}

	public synchronized void addPost(Community c) {
		communitydao.insert(c);
	}

	public synchronized void setPost(Community c) {
		communitydao.update(c);
	}

	public synchronized Object[] deletePost(long listId) {
		Object[] communities = communitydao.selectById(listId);
		communitydao.delete(listId);
		return communities;

	}

	// 게시물 리스트 분류에 따라 셀렉트
	public List<Object[]> getPostByType(String type) {
		List<Object[]> rtn = null;

		if (type.equals("pro")) {
			rtn = communitydao.selectByPro();
		}
		if (type.equals("normal")) {
			rtn = communitydao.selectByPeople();
		}
		if (type.equals("views")) {
			rtn = communitydao.selectByViews();
		} // 인화 추가 1025

		return rtn;
	}

	public Community getByCommunityId(long listId) {
		return communitydao.selectByCommunityId(listId);
	}

	// 조회수 올리는 함수
	public void addViews(long listId) {
		communitydao.updateViews(listId);
	}

	// 관리자의 아이디 조회
	public List<Community> usernameFind(String username) {
		List<Community> rtn = new ArrayList<>();
		List<Object[]> allList = null;

		try {
			allList = communitydao.getSearchByUserName(username);
			if (allList == null) {
				System.out.println("username 조회 불가능, 유저가 작성한 게시물이 없습니다.");
				return rtn;
			}
			for (Object[] o : allList) {
				Community c = new Community();
				c.setId(castLong(o[0]));
				c.setContent(castString(o[1]));
				c.setImage(castString(o[2]));
				c.setDate(castDate(o[3]));
				c.setMemberId(castLong(o[4]));
				c.setTitle(castString(o[5]));
				c.setUsername(castString(o[6]));
				c.setMemberType(castString(o[7]));
				c.setViews(castLong(o[8]));
				c.setReplies(castLong(o[9]));
				rtn.add(c);
			}
		} catch (Exception e) {
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
	
	//조회수 높은 순 게시물 두 개 셀렉트
	public List<Community> getPostByViews() {
		List<Community> rtn = communitydao.selectByViewsHome();
		return rtn;
	}
}
