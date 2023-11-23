package com.damoyeo.healthyLife.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostMessage {
	long PostMsgId;
	String recieveName;
	String senderName;
	String title;
	String content;
	Date date;
	long trainerId;
	long memberId;
}
