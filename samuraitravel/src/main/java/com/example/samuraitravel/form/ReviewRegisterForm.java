package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * レビュー投稿フォーム
 */
@Data
public class ReviewRegisterForm {
	/** 評価 */
	@NotBlank(message = "評価を入力してください。")
	private String evaluation;

	/** コメント */
	@NotBlank(message = "内容を入力してください。")
	private String detail;
}
