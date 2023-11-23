package com.damoyeo.healthyLife.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.PostMessage;
import com.damoyeo.healthyLife.dao.PostMessageDAO;

@Service
public class PostMessageService {
	@Autowired
	PostMessageDAO postMessagedao;
	
	public void addMessage(PostMessage pmg) throws ParseException{
		postMessagedao.insert(pmg);
	}
	
	public List<PostMessage> getAllMessages() {
		return postMessagedao.Select();
	}
	
	public void deleteMessage(long id) {
		postMessagedao.delete(id);
	}
	public List<PostMessage> getAllMessagesByUserId(long userId){
		return postMessagedao.SelectByUserId(userId);
	}
	public List<PostMessage>getAllMessagesByPsgId(long postMsgId){
		return postMessagedao.SelectByPostMsgId(postMsgId);
	}
	public List<PostMessage> editMessage(PostMessage pmg){
		postMessagedao.update(pmg);
		return postMessagedao.SelectByUserId(pmg.getMemberId());
	}
}
