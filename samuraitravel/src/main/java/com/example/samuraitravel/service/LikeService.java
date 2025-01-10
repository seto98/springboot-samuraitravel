package com.example.samuraitravel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Like;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.LikeRepository;
import com.example.samuraitravel.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * お気に入り用のサービスクラス
 */
@Service
public class LikeService {
	private final LikeRepository likeRepository;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;

	public LikeService(LikeRepository likeRepository, HouseRepository houseRepository, UserRepository userRepository) {
		this.likeRepository = likeRepository;
		this.houseRepository = houseRepository;
		this.userRepository = userRepository;
	}

	/**
	 * お気に入り一覧取得
	 */
	public Page<Like> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable) {
		return likeRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
	}

	/**
	 * 指定されたユーザーの特定の民宿に関するお気に入り登録状況取得
	 */
	public List<Like> findByUserIdAndHouseIdOrderByCreatedAtDesc(Integer userId, Integer houseId) {
		return likeRepository.findByUserIdAndHouseIdOrderByCreatedAtDesc(userId, houseId);
	}

	/**
	 * お気に入り追加
	 */
	@Transactional
	public void createLike(Integer userId, Integer houseId) {
		// ユーザー情報をid検索
		Optional<User> optionalUser = userRepository.findById(userId);
		User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("指定されたIDのユーザーが存在しません。"));
		// 民宿情報をid検索
		Optional<House> optionalHouse = houseRepository.findById(houseId);
		House house = optionalHouse.orElseThrow(() -> new EntityNotFoundException("指定されたIDの民宿が存在しません。"));

		// Entityに詰める
		Like like = new Like();
		like.setHouse(house);
		like.setUser(user);

		// 登録
		likeRepository.save(like);
	}

	/**
	 * お気に入り解除
	 */
	@Transactional
	public void deleteByUserIdAndHouseId(Integer userId, Integer houseId) {
		// 削除
		likeRepository.deleteByUserIdAndHouseId(userId, houseId);
	}
}
