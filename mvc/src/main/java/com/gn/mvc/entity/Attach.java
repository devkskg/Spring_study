package com.gn.mvc.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
@Table(name="attach")
public class Attach {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="attach_no")
	private Long attachNo;
	
	@Column(name="ori_name")
	private String oriName;
	@Column(name="new_name")
	private String newName;
	@Column(name="attach_path")
	private String attachPath;
	
	
	@ManyToOne
	@JoinColumn(name="board_no")
	private Board board;
	
}
