package com.example.samuraitravel.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * レビュー情報
 */
@Entity
@Table(name = "likes")
@Data
public class Like {
	/** レビューID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/** 民宿情報 */
	@ManyToOne
	@JoinColumn(name = "house_id")
	private House house;

	/** ユーザー情報 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	/** 作成日時 */
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;

	/** 更新日時 */
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;
}
