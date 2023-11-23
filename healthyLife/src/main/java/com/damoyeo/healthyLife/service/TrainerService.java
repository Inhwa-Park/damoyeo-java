package com.damoyeo.healthyLife.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.Trainer;
import com.damoyeo.healthyLife.dao.TrainerDAO;

@Service
public class TrainerService {
	@Autowired
	TrainerDAO trainerdao;
	
	public void addInfo (long userId ,String experience, String introduce, String img) throws Exception{ //트레이너 info 등록
		Trainer t = trainerdao.findAccountById(userId);
		t.setExperience(experience);
		t.setIntroduce(introduce);
		t.setImg(img);
		trainerdao.insertInfo(t);
	}
	
	public List<Trainer> findAll() {
		return trainerdao.select();
	}
	public Long findByUsername(String username) throws Exception {
		return trainerdao.selectByUsername(username);
	}
	public Member findUserOrTrainer(long trainerId)throws Exception  {
		
		Member member = new Member();
		member.setRole(trainerdao.findUserOrTrainer(trainerId));
		return member;
	}
	
	
}