package com.example.samuraitravel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * レビューDTO
 */
@Data
@AllArgsConstructor
public class ReviewDTO {
	/** 民宿ID */
	private Integer houseId;

	/** ユーザーID */
	private Integer userId;

	/** 評価 */
	private String evaluation;

	/** コメント */
	private String detail;
}