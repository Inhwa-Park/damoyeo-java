package com.damoyeo.healthyLife.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.damoyeo.healthyLife.bean.Cart;
import com.damoyeo.healthyLife.bean.GeneralUser;
import com.damoyeo.healthyLife.bean.Ordering;
import com.damoyeo.healthyLife.bean.Product;
import com.damoyeo.healthyLife.dto.ResponseDTO;
import com.damoyeo.healthyLife.service.ShopService;

@RestController
@RequestMapping("/shopApi")
public class ShopApiController {

	@Autowired
	ShopService shopService;

	// 상품 목록 전체 가져오기
	@GetMapping("/getAllProduct")
	public ResponseEntity<?> getAllProduct() throws Exception {
		List<Product> viewList = shopService.findAllProduct();
		return ResponseEntity.ok().body(viewList);
	}
	
	//할인 상품 목록만 가져오기
	@GetMapping("/getAllSaleProduct")
	public ResponseEntity<?> getAllSaleProduct() throws Exception {
		List<Product> viewList = shopService.findAllSaleProduct();
		return ResponseEntity.ok().body(viewList);
	}

	// 이미지 가져오기
	@GetMapping("/getImage/{filename}")
	public ResponseEntity<Resource> downloadExecute(@PathVariable String filename) throws IOException {
		Path path = Paths.get("C:\\IMAGE_DATA\\" + filename);
		String contentType = Files.probeContentType(path);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		Resource resource = new InputStreamResource(Files.newInputStream(path));

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}
	
	//상품 하나 id로 조회해서 가져오기
	@GetMapping("/getProduct/{id}")
	public ResponseEntity<?> getProductById(@PathVariable long id) throws Exception {
		Product product = shopService.findProduct(id);
		return ResponseEntity.ok().body(product);
	}
	
	//식품, 운동용품 별로 목록 가져오기
	@GetMapping("/getAllByType/{type}")
	public ResponseEntity<?> getAllByType(@PathVariable String type) throws Exception {
		List<Product> viewList = shopService.findAllByType(type);
		return ResponseEntity.ok().body(viewList);
	}
	
	//할인 상품 중 식품, 운동용품 별로 가져오기
	@GetMapping("/getAllSaleByType/{type}")
	public ResponseEntity<?> getAllSaleByType(@PathVariable String type) throws Exception {
		List<Product> viewList = shopService.findAllSaleByType(type);
		return ResponseEntity.ok().body(viewList);
	}
	
	//장바구니에 상품 넣기
	@PostMapping("/addCart")
	public ResponseEntity<?> addCart(
			@AuthenticationPrincipal GeneralUser gerneralUser, @RequestBody Cart cart) throws Exception{
		cart.setMemberId(gerneralUser.getId());
		shopService.addCart(cart);
		ResponseDTO responseDTO = ResponseDTO.builder().msg("상품을 장바구니에 담았습니다.").build();
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//장바구니 목록 가져오기
	@GetMapping("/getAllCart/{memberId}")
	public ResponseEntity<?> getAllCart(@PathVariable long memberId) throws Exception {
		List<Cart> viewList = shopService.findAllCart(memberId);
		return ResponseEntity.ok().body(viewList);
	}
	
	//장바구니 수량 변경하기
	@PostMapping("/updateCart")
	public ResponseEntity<?> updateCart(
			@AuthenticationPrincipal GeneralUser gerneralUser, @RequestBody Cart cart) throws Exception{
		shopService.updateAmount(cart);
		ResponseDTO responseDTO = ResponseDTO.builder().msg("상품의 수량을 변경했습니다.").build();
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//장바구니 상품 삭제하기
	@GetMapping("/deleteCart/{productId}")
	public ResponseEntity<?> deleteCart(
			@AuthenticationPrincipal GeneralUser gerneralUser, @PathVariable long productId) throws Exception{
		shopService.deleteById(productId);
		ResponseDTO responseDTO = ResponseDTO.builder().msg("상품을 장바구니에서 삭제했습니다.").build();
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//장바구니 상품 전체 삭제하기
	@GetMapping("/deleteCartAll")
	public ResponseEntity<?> deleteCartAll(
			@AuthenticationPrincipal GeneralUser gerneralUser) throws Exception{
		shopService.deleteAll();
		ResponseDTO responseDTO = ResponseDTO.builder().msg("상품을 장바구니에서 삭제했습니다.").build();
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//주문 상품 ordering 테이블에 넣기
	@PostMapping("/addOrdering")
	public ResponseEntity<?> addOrdering(
			@AuthenticationPrincipal GeneralUser gerneralUser, @RequestBody Ordering ordering) throws Exception{
		ordering.setOrderDate(new Date());
		int result = shopService.addOrdering(ordering);
		
		if(result > 0) {
			//주문 테이블에 넣기 성공 시
			ResponseDTO responseDTO = ResponseDTO.builder().msg("상품 주문이 완료되었습니다.").build();
			return ResponseEntity.ok().body(responseDTO);
		}else {
			//주문 테이블 넣기 실패 시
			ResponseDTO responseDTO = ResponseDTO.builder().error("상품 주문에 실패했습니다.").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	//주문 목록 가져오기
	@GetMapping("/getAllOrdering/{memberId}")
	public ResponseEntity<?> getAllOrdering(@PathVariable long memberId) throws Exception {
		List<Ordering> viewList = shopService.findAllOrdering(memberId);
		return ResponseEntity.ok().body(viewList);
	}
	
	//기간별 주문 내역 목록 가져오기
	@GetMapping("/getAllOrderingByPeriod/{startDate}/{endDate}")
	public ResponseEntity<?> getAllOrderingByPeriod(
			@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	        @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws Exception {
	    List<Ordering> viewList = shopService.SelectByPeriod(startDate, endDate);
	    return ResponseEntity.ok().body(viewList);
	}
}
