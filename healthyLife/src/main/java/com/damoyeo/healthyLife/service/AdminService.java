package com.damoyeo.healthyLife.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.damoyeo.healthyLife.bean.AdminUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.dao.AdminUserDAO;
import com.damoyeo.healthyLife.dao.MemberDAO;
import com.damoyeo.healthyLife.exception.NotUserException;

@Service
public class AdminService implements UserDetailsService{
	@Autowired
	AdminUserDAO admindao;
    
	 @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        // 여기에서 username을 기반으로 데이터베이스에서 사용자 정보를 가져오는 작업을 수행합니다.
	        // 만약 username에 해당하는 사용자가 없다면 UsernameNotFoundException을 던집니다.

	        // 예시: username이 "admin"이라고 가정하고, 해당 사용자가 Admin 권한을 가지고 있다고 가정합니다.
	        if (username.equals("관리자")) {
	            return org.springframework.security.core.userdetails.User.builder()
	                    .username(username)
	                    .password("adminpassword") // 실제로는 암호화된 비밀번호를 사용해야 합니다.
	                    .roles("ADMIN")
	                    .build();
	        }

	        // 다른 사용자의 경우 여기에서 로직을 추가하여 UserDetails를 생성합니다.
	        // 예를 들어, 데이터베이스에서 사용자 정보를 조회하고 UserDetails로 변환하는 작업을 수행합니다.

	        throw new UsernameNotFoundException("User not found with username: " + username);
	    }
	 
	 public String findUsernameById(String id) throws Exception {
			AdminUser admin =  admindao.selectByAdminUserId(id);
			String username =  admin.getAdminName();
		
				if (admin.getUsername() == null) {
					// 아이디가 존재하지 않을 경우 ==> 예외 발생
					throw new NotUserException(admin.getUsername() + " (이)란 아이디는 존재하지 않아요");
				}

				return username;// 해당 회원 반환	
		}
	 
	public AdminUser getByCredentials(final String username, final String password) throws Exception {
		AdminUser admin =  admindao.selectByAdminUsername(username);
		 
	
			if (admin.getUsername() == null) {
				// 아이디가 존재하지 않을 경우 ==> 예외 발생
				throw new NotUserException(username + " (이)란 아이디는 존재하지 않아요");
			}

			// 비밀번호 체크
			String dbPwd = admin.getPassword();
			if (!password.equals(dbPwd)) {
				// 비밀번호가 불일치라면
				 throw new NotUserException("비밀번호가 일치하지 않아요");
			
			}
			return admin;// 해당 회원 반환	
	}
}
