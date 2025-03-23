package com.gn.mvc.entity;

import java.time.LocalDateTime;
import java.util.List;

import groovy.transform.builder.Builder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="chat_room")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="room_no")
	private Long roomNo;
	
	@Column(name="last_msg")
	private String lastMsg;
	
	@Column(name="last_date")
	private LocalDateTime lastDate;
	
	// 자세하게 하면 manytoone이 맞으나 간단하게 수업하기 위해 onetoone으로 한다.
	@OneToOne
	@JoinColumn(name="from_member")
	private Member fromMember;
	@OneToOne
	@JoinColumn(name="to_member")
	private Member toMember;
	
	@OneToMany(mappedBy="chatRoom")
	private List<ChatMsg> chatMsgs;
}
