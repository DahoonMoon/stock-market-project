package com.project.stock.model.entity.member;

import com.project.stock.model.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", updatable = false, nullable = false)
	private Integer id;

	@Comment("사용자 이름")
	@Column(nullable = false)
	private String userName;

	@Comment("비고")
	@Column(length = 200)
	private String remark;


	public Member(String userName) {
		this.userName = userName;
	}
}
