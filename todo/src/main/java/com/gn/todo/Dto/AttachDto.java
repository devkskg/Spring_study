package com.gn.todo.Dto;

import com.gn.todo.Entity.Attach;

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
public class AttachDto {

	private Long attach_no;
	private String ori_name;
	private String new_name;
	private String attach_path;
	
	public Attach toEntity() {
		return Attach.builder()
				.attachNo(attach_no)
				.oriName(ori_name)
				.newName(new_name)
				.attachPath(attach_path)
				.build();
	}
	public AttachDto toDto(Attach entity) {
		return AttachDto.builder()
				.attach_no(entity.getAttachNo())
				.ori_name(entity.getOriName())
				.new_name(entity.getNewName())
				.attach_path(entity.getAttachPath())
				.build();
	}
	
}
