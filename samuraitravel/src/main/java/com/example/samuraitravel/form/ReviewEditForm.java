package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * レビュー編集フォーム
 */
@Data
@AllArgsConstructor
public class ReviewEditForm {
	/** 評価 */
	@NotBlank(message = "評価を入力してください。")
	private String evaluation;

	/** コメント */
	@NotBlank(message = "コメントを入力してください。")
	private String detail;
}
