package com.example.samuraitravel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * レビュー用のサービスクラス
 */
@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;

	public ReviewService(ReviewRepository reviewRepository, HouseRepository houseRepository,
			UserRepository userRepository) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.userRepository = userRepository;
	}
	
	/**
	 * レビュー一覧表示
	 */
	public Page<Review> findByHouseIdOrderByCreatedAtDesc(Integer houseId, Pageable pageable) {
		return reviewRepository.findByHouseIdOrderByCreatedAtDesc(houseId, pageable);
	}
	public List<Review> findByHouseIdOrderByCreatedAtDescList(Integer houseId) {
		return reviewRepository.findByHouseIdOrderByCreatedAtDesc(houseId);
	}

	/**
	 * レビュー表示（6件のみ）
	 */
	public List<Review> findTop6ByHouseIdOrderByCreatedAtDesc(Integer houseId) {
		return reviewRepository.findTop6ByHouseIdOrderByCreatedAtDesc(houseId);
	}

	/**
	 * レビュー投稿
	 */
	@Transactional
	public void createReview(ReviewRegisterForm reviewRegisterForm, Integer houseId, Integer userId) {
		Review review = new Review();

		// 民宿情報をid検索する
		Optional<House> optionalHouse = houseRepository.findById(houseId);
		House house = optionalHouse.orElseThrow(() -> new EntityNotFoundException("指定されたIDの民宿が存在しません。"));
		// ユーザー情報をid検索する
		Optional<User> optionalUser = userRepository.findById(userId);
		User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("指定されたIDのユーザーが存在しません。"));
		// レビュー投稿用フォームから情報を取得
		String evaluation = reviewRegisterForm.getEvaluation();
		String detail = reviewRegisterForm.getDetail();

		// Entityに詰める
		review.setHouse(house);
		review.setUser(user);
		review.setEvaluation(evaluation);
		review.setDetail(detail);

		reviewRepository.save(review);
	}

	/**
	 * レビュー編集
	 */
	// レビューをidで検索
	public Optional<Review> findReviewById(Integer id) {
		return reviewRepository.findById(id);
	}
	
	// レビューを更新
	@Transactional
	public void updateReview(ReviewEditForm reviewEditForm, Review review) {
		review.setEvaluation(reviewEditForm.getEvaluation());
		review.setDetail(reviewEditForm.getDetail());
		reviewRepository.save(review);
	}

	/**
	 * レビュー削除
	 */
	@Transactional
	public void deleteReview(Review review) {
		reviewRepository.delete(review);
	}
}
