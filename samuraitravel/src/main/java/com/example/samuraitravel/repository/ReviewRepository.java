package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Review;

/**
 * レビュー用リポジトリ
 */
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	// レビュー一覧表示
	public Page<Review> findByHouseIdOrderByCreatedAtDesc(Integer houseId, Pageable pageable);
	public List<Review> findByHouseIdOrderByCreatedAtDesc(Integer houseId);

	// レビュー表示（6件のみ）
	public List<Review> findTop6ByHouseIdOrderByCreatedAtDesc(Integer houseId);
}