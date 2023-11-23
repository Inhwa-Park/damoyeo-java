package com.damoyeo.healthyLife.dto;

import java.util.List;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class ChatMessageDTO {
	private String Message;
	 private String writer; //입장할때
	 private String outer; //퇴장할때 
	 private String speaker; //대화 할떄
	 private String date;
}
