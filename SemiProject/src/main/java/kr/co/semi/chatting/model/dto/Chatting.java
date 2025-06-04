package kr.co.semi.chatting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chatting {
	
	private int messageNo;
    private int studyNo;
    private int senderNo;
    private String content;
    private String sendTime;
    private String profileImg;

}
