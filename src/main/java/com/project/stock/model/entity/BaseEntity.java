package com.project.stock.model.entity;

import java.time.LocalDateTime;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity {

	private LocalDateTime createdDate;
	private String createdBy = "SYSTEM";
	private LocalDateTime modifiedDate;
	private String modifiedBy = "SYSTEM";

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdDate = now;
		this.modifiedDate = now;
	}

	@PreUpdate
	public void preUpdate() {
		this.modifiedDate = LocalDateTime.now();
	}

}
