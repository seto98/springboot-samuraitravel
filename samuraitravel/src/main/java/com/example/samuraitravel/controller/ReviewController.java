package com.example.samuraitravel.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.HouseService;
import com.example.samuraitravel.service.ReviewService;

import jakarta.servlet.http.HttpSession;

/**
 * レビューページ・機能を制御
 */
@Controller
@RequestMapping("/reviews")
public class ReviewController {
	private final ReviewService reviewService;
	private final HouseService houseService;

	// コンストラクター
	public ReviewController(ReviewService reviewService, HouseService houseService) {
		this.reviewService = reviewService;
		this.houseService = houseService;
	}

	// レビュー表示（一覧表示）
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model, HttpSession session) {
		Page<Review> reviewPage;
		// 全件検索
		Integer houseId = (Integer) session.getAttribute("houseId");
		Optional<House> optionalHouse = houseService.findHouseById(houseId);
		House house = optionalHouse.get();
		model.addAttribute("house", house);

		reviewPage = reviewService.findByHouseIdOrderByCreatedAtDesc(houseId, pageable);
		model.addAttribute("reviewPage", reviewPage);

		Integer userId = null;
		if (userDetailsImpl != null) {
			User user = userDetailsImpl.getUser();
			userId = user.getId();
			model.addAttribute("userId", userId);
		}

		return "reviews/index";
	}

	// レビュー投稿用フォーム表示
	@GetMapping("/{houseId}/register")
	public String register(@PathVariable(name = "houseId") Integer houseId, Model model) {
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());

		Optional<House> optionalHouse = houseService.findHouseById(houseId);
		House house = optionalHouse.get();
		model.addAttribute("house", house);
		return "reviews/register";
	}

	// レビュー投稿
	@PostMapping("/{houseId}/create")
	public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult,
			@PathVariable(name = "houseId") Integer houseId, RedirectAttributes redirectAttributes, Model model) {
		// エラーチェック
		if (bindingResult.hasErrors()) {
			model.addAttribute("reviewRegisterForm", reviewRegisterForm);
			return "reviews/register";
		}

		Integer userId = null;
		if (userDetailsImpl != null) {
			User user = userDetailsImpl.getUser();
			userId = user.getId();
		}

		reviewService.createReview(reviewRegisterForm, houseId, userId);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
		return "redirect:/houses/" + houseId;
	}

	// レビューの編集
	@GetMapping("/{houseId}/edit")
	public String edit(@PathVariable(name = "houseId") Integer houseId, RedirectAttributes redirectAttributes,
			Model model) {
		Optional<Review> optionalReview = reviewService.findReviewById(houseId);
		// エラーチェック
		if (optionalReview.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "レビューが存在しません。");
			return "redirect:/houses/" + houseId;
		}

		Review review = optionalReview.get();
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getEvaluation(), review.getDetail());
		// モデルに詰める
		model.addAttribute("review", review);
		model.addAttribute("reviewEditForm", reviewEditForm);
		model.addAttribute("houseId", houseId);

		return "reviews/edit";
	}

	// レビューの更新
	@PostMapping("/{reviewId}/update")
	public String update(@ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult,
			@PathVariable(name = "reviewId") Integer reviewId, RedirectAttributes redirectAttributes, Model model,
			HttpSession session) {
		Optional<Review> optionalReview = reviewService.findReviewById(reviewId);
		Integer houseId = (Integer) session.getAttribute("houseId");
		// 存在チェック
		if (optionalReview.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "レビューが存在しません。");
			return "redirect:/houses/" + houseId;
		}
		Review review = optionalReview.get();

		// エラーチェック
		if (bindingResult.hasErrors()) {
			model.addAttribute("review", review);
			model.addAttribute("reviewEditForm", reviewEditForm);
			return "reviews/edit";
		}

		reviewService.updateReview(reviewEditForm, review);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
		return "redirect:/houses/" + houseId;
	}

	// レビューの削除
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Optional<Review> optionalReview = reviewService.findReviewById(id);
		Integer houseId = (Integer) session.getAttribute("houseId");
		// レビューの存在チェック
		if (optionalReview.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "レビューが存在しません。");
			return "redirect:/houses/" + houseId;
		}
		// レビュー情報取得
		Review review = optionalReview.get();
		// 削除
		reviewService.deleteReview(review);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");

		return "redirect:/houses/" + houseId;
	}
}
