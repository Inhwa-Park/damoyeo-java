package com.damoyeo.healthyLife.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.damoyeo.healthyLife.bean.Community;
import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Member;
import com.damoyeo.healthyLife.bean.WiseSaying;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.service.CommentService;
import com.damoyeo.healthyLife.service.CommunityService;
import com.damoyeo.healthyLife.service.MemberService;

@RestController
@MultipartConfig(maxFileSize=1024*1024*2, location="C:\\IMAGE_DATA\\")
@RequestMapping("/coummunityApi")
public class CommunityApiController {
	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private CommentService commentService;
	
	//게시물 리스트 조회, 게시물 검색 기능
	@GetMapping(value={"/communityList/{searchTypeParam}/{searchContentParam}", "/communityList"}) //1019 인화 수정
	public ResponseEntity<?>  communityList(
			@AuthenticationPrincipal GeneralUser generalUser,  
			@PathVariable(required = false) String searchTypeParam, 
			@PathVariable(required = false) String searchContentParam) throws Exception {
		
		List<Community> viewList = new ArrayList<>();
		List<Object[]> allList = communityService.getAll(
				StringUtils.defaultIfEmpty(searchTypeParam, null), StringUtils.defaultIfEmpty(searchContentParam, null));
			
			for(Object[] o : allList) {
				Community c = new Community();
				c.setId(castLong(o[0]));
				c.setContent(castString(o[1]));
				c.setImage(castString(o[2]));
				c.setDate((java.sql.Date)(o[3]));
				c.setMemberId(castLong(o[4]));
				c.setTitle(castString(o[5]));
				c.setUsername(castString(o[6]));
				c.setMemberType(castString(o[7]));
				c.setViews(castLong(o[8]));
				c.setReplies(castLong(o[9]));
				commentService.addViewsAtCommunity(castLong(o[0]));
				
				viewList.add(c);			
			}
			return ResponseEntity.ok().body(viewList);
			}
	
	//타입별 게시물 리스트 조회 기능(일반 글만, 전문 글만, 조회수 높은 순) -인화 추가 1025
	@GetMapping("/communityListByType/{listType}") 
	public ResponseEntity<?>  communityListByType(
			@AuthenticationPrincipal GeneralUser generalUser, @PathVariable String listType) throws Exception {
		
		List<Community> viewList = new ArrayList<>();
		List<Object[]> allList = communityService.getPostByType(listType);
			
			for(Object[] o : allList) {
				Community c = new Community();
				c.setId(castLong(o[0]));
				c.setContent(castString(o[1]));
				c.setImage(castString(o[2]));
				c.setDate((java.sql.Date)(o[3]));
				c.setMemberId(castLong(o[4]));
				c.setTitle(castString(o[5]));
				c.setUsername(castString(o[6]));
				c.setMemberType(castString(o[7]));
				c.setViews(castLong(o[8]));
				c.setReplies(castLong(o[9]));
				commentService.addViewsAtCommunity(castLong(o[0]));
				
				viewList.add(c);			
			}
			return ResponseEntity.ok().body(viewList);
			}
	
	//상세 게시물 조회
	@GetMapping("/communityInfo/{listId}") 
	public ResponseEntity<?>  communityInfo(@AuthenticationPrincipal GeneralUser generalUser, @PathVariable long listId) throws Exception{

		Community c = new Community();
		Object[] postObject = communityService.getPost(listId);
		c.setId(castLong(postObject[0]));
		c.setContent(castString(postObject[1]));
		c.setImage(castString(postObject[2]));
		c.setDate((java.sql.Date)(postObject[3]));
		c.setTitle(castString(postObject[4]));
		c.setMemberId(castLong(postObject[5]));
		c.setUsername(castString(postObject[6]));
		c.setMemberType(castString(postObject[7]));
		c.setViews(castLong(postObject[8]));
		c.setReplies(castLong(postObject[9]));
		
		return ResponseEntity.ok().body(c);
	}
	
	// 조회수 추가 - 위 인포 메서드 내에서 같이 실행될 경우 여러번 호출되면서 조회수 몇개씩 증가해서 따로 빼고, 게시물 인포 get할 때 조회수 증가 api 같이 호출해주는 걸로 수정. 1025 인화
	@GetMapping("/addViews/{listId}") 
	public void addViews(@AuthenticationPrincipal GeneralUser generalUser, @PathVariable long listId) throws Exception{
		communityService.addViews(listId); 
	}
	
	//글 등록, 수정
	@PostMapping(value={"/addPost"})
	public ResponseEntity<?> addPost(@AuthenticationPrincipal GeneralUser gerneralUser,
			//수정 시 게시물번호, 이미지 String을 커뮤니티 객체에 같이 넣어서 보내주기(ModelAttribute-MultipartFile이라 같이 받으려면 이렇게 해야함)
			@ModelAttribute Community communityParam, 
			@RequestPart(value = "file", required = false) MultipartFile[] files) throws Exception {

		long id = gerneralUser.getId();
		System.out.println("id:    " + gerneralUser.getId());

		long update = communityParam.getId(); //처음 작성이면 -1, 수정이면 해당 게시물 아이디
		Date nowDate = new Date();
		Date today = DateUtils.truncate(nowDate, Calendar.DATE);
		System.out.println("communityParamId:    " + communityParam.getId());

		try {
			// 수정이 아니라 처음 게시물 작성인 경우
			if (update == -1) { 
				
				// 이미지가 있는 경우
				if (files != null) { 
					String uuidPicName = null;
					uuidPicName = uploadFile(files, id);
					System.out.println("List<String> list:	" + uuidPicName);
					
				//list를 set해야함
				communityParam.setImage(uuidPicName);
				communityParam.setDate(today);
				communityParam.setMemberId(castLong(id));
				communityService.addPost(communityParam);
					
				// 이미지가 없는 경우
				} else { 
					communityParam.setDate(today);
					communityParam.setMemberId(castLong(id));
					communityService.addPost(communityParam);
				}
				
			// 게시물 수정 하는 경우	
			}else {
				// 수정시 이미지가 없는 경우
				if (files == null) { 
					communityParam.setImage(communityParam.getImage().equals("null") ? null : communityParam.getImage());
					communityParam.setId(communityParam.getId());
					communityParam.setDate(today);
					communityParam.setMemberId(castLong(id));
					
					communityService.setPost(communityParam);
				}
				
				// 수정 시 이미지가 있는 경우
				else if (files != null) {
					String uuidPicName = null;
					uuidPicName = uploadFile(files, id);
					
					//list를 set해야함
					communityParam.setImage(uuidPicName);
					communityParam.setDate(today);
					communityParam.setMemberId(castLong(id));
					communityService.setPost(communityParam);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Object[] postObject = communityService.getPost(communityParam.getId());
		communityParam.setId(castLong(postObject[0]));
		communityParam.setContent(castString(postObject[1]));
		communityParam.setImage(castString(postObject[2]));
		communityParam.setDate((java.sql.Date) (postObject[3]));
		communityParam.setTitle(castString(postObject[4]));
		communityParam.setMemberId(castLong(postObject[5]));
		communityParam.setUsername(castString(postObject[6]));
		communityParam.setMemberType(castString(postObject[7]));
		
		return ResponseEntity.ok().body(communityParam);
	}
	
		//파일명 재생성 메서드
		private String uploadFile(MultipartFile[] files, long id) throws Exception {
			String rtnFileName = ";";
			String saveName = null;
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
				saveName = uuid + "_" + fileName;
				
				Path savePath = Paths.get("C:\\IMAGE_DATA"+ File.separator + saveName);
				// Paths.get() 메서드는 특정 경로의 파일 정보를 가져옵니다.(경로 정의하기)

				try {
					uploadFile.transferTo(savePath);
					// uploadFile에 파일을 업로드 하는 메서드 transferTo(file)
				} catch (IOException e) {
					e.printStackTrace();
					// printStackTrace()를 호출하면 로그에 Stack trace가 출력됩니다.
				}
				rtnFileName =rtnFileName+saveName+";";
			}
			return rtnFileName;
		}

		//이미지 가져오기
		@GetMapping("/getImage/{filename}")
		public ResponseEntity<Resource> downloadExecute(@PathVariable String filename) throws IOException {
			System.out.println(filename);
			
			//파일명이 한글일 때 인코딩 작업
			String str = URLEncoder.encode(filename, "UTF-8");
			//원본파일명에서 공백이 있을 때, +로 표시되므로 공백으로 처리
			str = str.replaceAll("\\+", "%20");
			
			Path path = Paths.get("C:\\IMAGE_DATA\\" + filename);
			String contentType = Files.probeContentType(path);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, contentType);
			Resource resource = new InputStreamResource(Files.newInputStream(path));
			
			return new ResponseEntity<>(resource, headers, HttpStatus.OK);
		}
		
		//게시물 삭제
		@GetMapping("/deletePost/{listId}")
		public ResponseEntity<?> deletePost(@AuthenticationPrincipal GeneralUser gerneralUser, @PathVariable("listId") long listIdParam) {
			
			try {
				Object[] c = communityService.deletePost(listIdParam);
				
				MultipartConfig mc = getClass().getAnnotation(MultipartConfig.class);
				
				if(c[2] != null) {
					String n = c[2].toString();
					n = n.substring(1, n.length()-1);
					
					String fileName = n;	
					String[] names = fileName.split(";");
					
					for(String name : names) {
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

}
