package com.damoyeo.healthyLife.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.damoyeo.healthyLife.dto.ChatMessageDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StompChatApiController {
	@MessageMapping("/helloA/{room}")
	@SendTo(value = "/topic/chatA/{room}")
	 public ChatMessageDTO enterA(String name, String room) throws Exception {
		Thread.sleep(1000); // simulated delay
		ChatMessageDTO chatRoom = ChatMessageDTO.builder()
					.writer(name)
	                .build();
		return chatRoom;
    }
	@MessageMapping("/chatA/{room}")
    @SendTo(value = "/topic/chatA/{room}")
    public ChatMessageDTO messageA(@RequestBody ChatMessageDTO chatdto){
    	System.out.println("stompChat is doing");
    	ChatMessageDTO chatRoom = ChatMessageDTO.builder()
	                //.writer(chatdto.getWriter())
	                .Message(chatdto.getMessage())
	                .speaker(chatdto.getWriter())
	                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
	                .build();
		return chatRoom;
	}
	@MessageMapping("/endA/{room}")
	@SendTo(value = "/topic/chatA/{room}")
	 public ChatMessageDTO endA(String name, String room) throws Exception {
		Thread.sleep(1000); // simulated delay
		ChatMessageDTO chatRoom = ChatMessageDTO.builder()
					.outer(name)
	                .build();
		return chatRoom;
    }
	
	//B
	@MessageMapping("/helloB/{room}")
	@SendTo(value = "/topic/chatB/{room}")
	 public ChatMessageDTO enterB(String name, String room) throws Exception {
		Thread.sleep(1000); // simulated delay
		ChatMessageDTO chatRoom = ChatMessageDTO.builder()
					.writer(name)
	                .build();
		return chatRoom;
    }
	@MessageMapping("/chatB/{room}")
    @SendTo(value = "/topic/chatB/{room}")
    public ChatMessageDTO messageB(@RequestBody ChatMessageDTO chatdto){
    	ChatMessageDTO chatRoom = ChatMessageDTO.builder()
	                //.writer(chatdto.getWriter())
	                .Message(chatdto.getMessage())
	                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
	                .build();
		return chatRoom;
	}
	@MessageMapping("/endB/{room}")
	@SendTo(value = "/topic/chatB/{room}")
	 public ChatMessageDTO endB(String name, String room) throws Exception {
		Thread.sleep(1000); // simulated delay
		ChatMessageDTO chatRoom = ChatMessageDTO.builder()
					.outer(name)
	                .build();
		return chatRoom;
    }
	
	//C
		@MessageMapping("/helloC/{room}")
		@SendTo(value = "/topic/chatC/{room}")
		 public ChatMessageDTO enterC(String name, String room) throws Exception {
			Thread.sleep(1000); // simulated delay
			ChatMessageDTO chatRoom = ChatMessageDTO.builder()
						.writer(name)
		                .build();
			return chatRoom;
	    }
		@MessageMapping("/chatC/{room}")
	    @SendTo(value = "/topic/chatC/{room}")
	    public ChatMessageDTO messageC(@RequestBody ChatMessageDTO chatdto){
	    	ChatMessageDTO chatRoom = ChatMessageDTO.builder()
		                //.writer(chatdto.getWriter())
		                .Message(chatdto.getMessage())
		                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
		                .build();
			return chatRoom;
		}
		@MessageMapping("/endC/{room}")
		@SendTo(value = "/topic/chatC/{room}")
		 public ChatMessageDTO endC(String name, String room) throws Exception {
			Thread.sleep(1000); // simulated delay
			ChatMessageDTO chatRoom = ChatMessageDTO.builder()
						.outer(name)
		                .build();
			return chatRoom;
	    }
}
