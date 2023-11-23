package com.damoyeo.healthyLife.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.Trainer;

import com.damoyeo.healthyLife.dto.ResponseDTO;

import com.damoyeo.healthyLife.service.TrainerService;

@RestController
@RequestMapping("/trainerApi")
public class TrainerApiController {
	@Autowired
	private TrainerService trainerService;
	
	//이미지 가져오기
	@GetMapping("/getImage/{filename}")
	public ResponseEntity<Resource> downloadExecute(@PathVariable String filename) throws IOException {
	    System.out.println(filename);

	    Path path = Paths.get("C:\\IMAGE_DATA\\" + filename);
	    String contentType = Files.probeContentType(path);
	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_TYPE, contentType);
	    Resource resource = new InputStreamResource(Files.newInputStream(path));

	    return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}
	
	@PostMapping("/addInfo") //트레이너가 상세 정보를 입력하고 확인 버튼을 누르는 상황, 상세정보 insert
	public ResponseEntity<ResponseDTO> addInfo(@AuthenticationPrincipal GeneralUser gerneralUser, 
			@RequestParam("experience") String experienceParam, @RequestParam("introduce") String introduceParam,
			@RequestPart(value = "file", required = true) MultipartFile[] files) throws Exception {
	
		try {
			String uuidPicName = null;
			if(files != null) { //이미지가 있는 경우
				
				uuidPicName = uploadFile(files, gerneralUser.getId());
			}
			trainerService.addInfo(gerneralUser.getId(), experienceParam, introduceParam, uuidPicName);
			ResponseDTO responseDTO = ResponseDTO.builder().msg("요청하신 트레이너 정보 요청 반영되었습니다.").build();
			return ResponseEntity.ok().body(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseDTO responseDTO = ResponseDTO.builder().error("faild").build();
		return ResponseEntity.badRequest().body(responseDTO);
	}
	// 파일명 재생성 메서드
	private String uploadFile(MultipartFile[] files, long id) throws Exception {
		
		String saveName = null;
		String rtnFileName = null;
		for (MultipartFile uploadFile : files) {
			if (uploadFile.getContentType().startsWith("image") == false) {
				//savedFileNames.add("this file is not image type");
				return "this file is not image type";
			}
			String originalName = uploadFile.getOriginalFilename();
			String fileName = originalName.substring(originalName.lastIndexOf("//") + 1);

			System.out.println("fileName" + fileName);
			String uuid = UUID.randomUUID().toString();
			// 저장할 파일 이름 중간에 "_"를 이용하여 구분
			rtnFileName =uuid + "_" + fileName;
			saveName = "C:\\IMAGE_DATA"+ File.separator + uuid + "_" + fileName;
			Path savePath = Paths.get(saveName);
			// Paths.get() 메서드는 특정 경로의 파일 정보를 가져옵니다.(경로 정의하기)

			try {
				uploadFile.transferTo(savePath);
				// uploadFile에 파일을 업로드 하는 메서드 transferTo(file)
			} catch (IOException e) {
				e.printStackTrace();
				// printStackTrace()를 호출하면 로그에 Stack trace가 출력됩니다.
			}
		}
		return rtnFileName;
	}
	
	@GetMapping("/userOrTrainer")
	public ResponseEntity<ResponseDTO<Member>> userOrTrainer(@AuthenticationPrincipal GeneralUser gerneralUser) throws Exception{
		try {	
			Member member = trainerService.findUserOrTrainer(gerneralUser.getId());
			System.out.println("gerneralUser.getId(): "+ gerneralUser.getId());
			ResponseDTO<Member> responseDTO = ResponseDTO.<Member>builder().object(member).build();
			return ResponseEntity.ok().body(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseDTO responseDTO = ResponseDTO.builder().error("faild").build();
		return ResponseEntity.badRequest().body(responseDTO);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<ResponseDTO<Trainer>> getAll(@AuthenticationPrincipal GeneralUser gerneralUser) throws Exception{
		try {	
			List<Trainer> trainerList = trainerService.findAll();
			System.out.println(trainerList);;
			ResponseDTO<Trainer> responseDTO = ResponseDTO.<Trainer>builder().msg("요청하신 트레이너 전체 목록이 도착했습니다.").data(trainerList).build();
			return ResponseEntity.ok().body(responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseDTO responseDTO = ResponseDTO.builder().error("faild").build();
		return ResponseEntity.badRequest().body(responseDTO);
	}
}
