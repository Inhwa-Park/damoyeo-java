package com.damoyeo.healthyLife.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.Trainer;
import com.damoyeo.healthyLife.dao.MemberDAO;
import com.damoyeo.healthyLife.dao.TrainerDAO;
import com.damoyeo.healthyLife.exception.MemberException;
import com.damoyeo.healthyLife.exception.NotUserException;

import lombok.RequiredArgsConstructor;

@Service
public class MemberService implements UserDetailsService {
	@Autowired
	MemberDAO memberdao;
	
	@Autowired
	TrainerDAO trainerdao;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public MemberService() {
		memberdao = new MemberDAO();
	}
	
	public String findUsernameById(String id) throws Exception {
		Member member =  memberdao.findAccountById(Long.valueOf(id));
		String username =  member.getUsername();
	
			if (member.getUsername() == null) {
				// 아이디가 존재하지 않을 경우 ==> 예외 발생
				throw new NotUserException(member.getUsername() + " (이)란 아이디는 존재하지 않습니다.");
			}

			return username;// 해당 회원 반환	
	}
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	try {
		 Member m =  memberdao.selectByUsername(username);
		 return GeneralUser.builder()
	        		.id(m.getId())
	                .username(username)
	                .password(m.getPassword()) // 실제로는 암호화된 비밀번호를 사용해야 합니다.
	                .role("USER")
	                .build();   
	}catch(Exception e) {
		e.printStackTrace();

	}
	return GeneralUser.builder()
    		.id(-1)
            .username(username)
            .password("-1") 
            .role("USER")
            .build();   
        
 }
	 
	// 로그인 판단 메서드
	public Member getByCredentials(final String username, final String password) throws Exception {
		Member original = memberdao.selectByUsername(username);

		if (original.getUsername() == null) {
			// 아이디가 존재하지 않을 경우 ==> 예외 발생
			throw new NotUserException(username + " (이)란 아이디는 존재하지 않습니다.");
		}

		// 비밀번호 체크
		String dbPwd = original.getPassword();
		if (!password.equals(dbPwd)) {
			// 비밀번호가 불일치라면
			throw new NotUserException("비밀번호가 일치하지 않습니다.");
		}
		return original;// 해당 회원 반환
	}

	// 유저네임 중복 판단 메서드
	public boolean checkUserNameExistence(String userName) {
		try {
			boolean result = memberdao.checkByUserName(userName);

			if (result) { // 닉네임 중복없음, 회원가입가능
				return true;
			} else { // 불가능
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 이메일 중복 판단 메서드
	public boolean CheckEmailExistence(String email) {
		boolean EmailResult = true;
		try {
			EmailResult = memberdao.checkEmail(email);

			if (EmailResult == false) {
				System.out.println("이메일 중복 이여서 안됨");
				return EmailResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EmailResult;
	}

	// 회원가입 판단 메서드
	public void addOrRefuse(Member member, String type) throws MemberException {
		try {
			boolean resultUserName = memberdao.checkByUserName(member.getUsername());
			boolean resultEmail = memberdao.checkEmail(member.getEmail());

			if(resultUserName && resultEmail) {
				if(!type.equals("일반")) { //트레이너
					memberdao.addMember(member);
					Member newMember =memberdao.selectByUsername(member.getUsername());
					Trainer trainer = new Trainer();
					trainer.setUserName(newMember.getUsername());
					trainer.setPassword(newMember.getPassword());
					trainer.setMemberId(newMember.getId());
					trainerdao.addTrainer(trainer);
					
				}else {
					memberdao.addMember(member);
				}
				
			}else {
				System.out.println("안됨");
				throw new MemberException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 계정삭제 함수
	public void resignAccount(long userId) throws Exception {
		memberdao.deleteAccountById(userId);
	}

	// 비밀번호 수정 메서드
	public int editPassword(Member member) throws Exception {
		int result = memberdao.updatePwdByUserName(member);
		if (result > 0) {
			return 1;
		}
		return -1;
	}

	// 비밀번호 찾기 메서드
	public String findPwd(String userName) throws Exception {
		Member m = memberdao.selectByUsername(userName);
		return m.getPassword();
	}

	// 아이디 찾기 메서드
	public String findId(String email) throws Exception {
		Member m = memberdao.selectByEmail(email);
		return m.getUsername();
	}

	// 비밀번호 찾기 시 아이디-이메일 일치하는 회원 있는지 확인
	public Member getMemberExistence(String userName, String email) throws Exception {
		Member m = memberdao.selectByUserNameAndEmail(userName, email);

		// 일치하는 회원 없으면 예외 던지기
		if (m.getUsername() == null) {
			throw new NotUserException("일치하는 회원 정보가 없습니다. 다시 입력해주세요.");
		}

		return m;// 해당 회원 반환
	}

	// id값으로 계정정보 찾는 함수
	public Member findAccount(long userId) throws Exception {
		Member m = memberdao.findAccountById(userId);
		return m;
	}
	
	//AdminApi 에서 사용
	public Member findIdByUsername(String username) throws Exception {
		 Member m =  memberdao.selectByUsername(username);
		 return m;
	}

	// userName으로 id 찾는 함수
	public long findIdByUserName(String UserName) throws Exception {
		Member m = memberdao.selectByUsername(UserName);
		return m.getId();
	}
	
	//운동 유형검사 후 결과 저장
	public void setSportType(Member m) throws Exception {
		memberdao.updateSportType(m);
	}

	//식단 유형검사 후 결과 저장
	public void setMenuType(Member m) throws Exception {
		memberdao.updateMenuType(m);
	}
	
	//이메일 찾기
	public String findEmail(long userId) throws Exception {
		String email = memberdao.selectEmailById(userId);
		return email;
	}
	
	//유저네임으로 멤버 정보 가져오는 메서드
    public Object[] findAccountByUsername(String username) throws Exception {
    	Object[] m =  memberdao.selectObjectByUsername(username);
    	return m;
	}
    
    //프로필 이미지 업로드
	public void updateImage(String p, Long pro) {
		trainerdao.updateProfile(p, pro);
	}
	
	//회원 전체 목록 가져오기
	public List<Member> findAllUser() throws Exception {
		List<Member> rtn = memberdao.select();
		return rtn;
	}
	
	//일반, 전문 회원 목록만 가져오기
	public List<Member> findAllUserByType(String type) throws Exception {
		List<Member> rtn = memberdao.selectAllByType(type);
		return rtn;
	}
}
