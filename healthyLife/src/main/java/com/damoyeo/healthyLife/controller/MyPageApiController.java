package com.damoyeo.healthyLife.controller;

import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.exception.MemberException;
import com.damoyeo.healthyLife.exception.NotUserException;
import com.damoyeo.healthyLife.service.MemberService;
import com.damoyeo.healthyLife.service.SportTestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mypageApi")
public class MyPageApiController {
	@Autowired
	private MemberService memberService;

	@GetMapping("/getUser/{username}")
	public ResponseEntity<?> mypageService(@AuthenticationPrincipal GeneralUser generalUser,
			@PathVariable String username) {
		try {
			Map<String, Object> map = new HashMap<>();
			Object[] member = memberService.findAccountByUsername(username);
			map.put("id", (member[0]));
			map.put("username", (member[1]));
			map.put("email", (member[2]));
			map.put("sportType", (member[3]));
			map.put("menuType", (member[4]));
			map.put("type", (member[5]));
			map.put("img", (member[6]));
			map.put("sNickname", (member[7]));
			map.put("mNickname", (member[8]));
			map.put("sContent", (member[9]));
			map.put("mContent", (member[10]));

			return ResponseEntity.ok().body(map);

		} catch (Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder().error("Searching failed.").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	@DeleteMapping("/resign")
	public ResponseEntity<?> resign(@AuthenticationPrincipal GeneralUser generalUser) throws Exception {
		try {
			memberService.resignAccount(generalUser.getId());
			// 추가: 로그아웃 처리
			SecurityContextHolder.clearContext();
			ResponseDTO responseDTO = ResponseDTO.builder().msg("계정이 성공적으로 삭제되었습니다.").build();
			return ResponseEntity.ok().body(responseDTO);

		} catch (MemberException e) {
			ResponseDTO responseDTO = ResponseDTO.builder().error("계정 삭제 중 오류가 발생했습니다.").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	@PostMapping("/uploadImage")
	public ResponseEntity<String> uploadImage(@AuthenticationPrincipal GeneralUser generalUser,
			@RequestPart("image") MultipartFile image) {

		if (image.isEmpty()) {
			return ResponseEntity.badRequest().body("이미지가 선택되지 않았습니다.");
		}

		try {
			// 이미지를 저장할 경로
			String uploadDir = "C:\\IMAGE_DATA";

			// 업로드 파일의 원래 이름
			String originalFileName = "profile_" + image.getOriginalFilename();

			// 이미지 파일을 저장할 경로
			String filePath = uploadDir + File.separator + originalFileName;

			// 이미지 파일을 서버에 저장
			File dest = new File(filePath);
			image.transferTo(dest);

			// db에 파일 이름 저장
			memberService.updateImage(originalFileName, generalUser.getId());

			// 이미지 업로드 성공 메시지 반환
			return ResponseEntity.ok("이미지가 성공적으로 업로드되었습니다. 경로: " + filePath);

		} catch (IOException e) {

			// 이미지 업로드 실패 메시지 반환
			return ResponseEntity.badRequest().body("이미지 업로드 중 오류가 발생했습니다.");
		}
	}

	// 이미지 가져오기
	@GetMapping("/getImage/{filename}")
	public ResponseEntity<Resource> downloadExecute(@PathVariable String filename) throws IOException {
		System.out.println(filename);

		// 파일명이 한글일 때 인코딩 작업
		String str = URLEncoder.encode(filename, "UTF-8");
		// 원본파일명에서 공백이 있을 때, +로 표시되므로 공백으로 처리
		str = str.replaceAll("\\+", "%20");

		Path path = Paths.get("C:\\IMAGE_DATA\\" + filename);
		String contentType = Files.probeContentType(path);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		Resource resource = new InputStreamResource(Files.newInputStream(path));

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

}
