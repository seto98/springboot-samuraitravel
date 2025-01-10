package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Like;

/**
 * お気に入り用リポジトリ
 */
public interface LikeRepository extends JpaRepository<Like, Integer> {
	// お気に入り一覧取得
	public Page<Like> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);

	// 指定されたユーザーの特定の民宿に関するお気に入り登録状況取得
	public List<Like> findByUserIdAndHouseIdOrderByCreatedAtDesc(Integer userId, Integer houseId);

	// お気に入り解除
	public void deleteByUserIdAndHouseId(Integer userId, Integer houseId);
}
