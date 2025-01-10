package com.example.samuraitravel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Like;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.HouseService;
import com.example.samuraitravel.service.LikeService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/likes")
public class LikeController {
	private final LikeService likeService;
	private final HouseService houseService;

	public LikeController(LikeService likeService, HouseService houseService) {
		this.likeService = likeService;
		this.houseService = houseService;
	}

	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			RedirectAttributes redirectAttributes, Model model, HttpSession httpSession) {
		// 民宿IDをセッション情報から取得
		Integer houseId = (Integer) httpSession.getAttribute("houseId");

		// ログイン中のユーザー情報を取得
		Integer userId = _getLoginUserDetail(userDetailsImpl);
		if (userId == 0) {
			redirectAttributes.addFlashAttribute("errorMessage", "ログインしてください。");
			return "redirect:/houses/" + houseId;
		}

		// お気に入り一覧を取得
		Page<Like> likePage;
		likePage = likeService.findByUserIdOrderByCreatedAtDesc(userId, pageable);
		model.addAttribute("likePage", likePage);

		// 民宿情報を詰めなおす
		List<Like> likesList = likePage.getContent();
		List<House> housesList = new ArrayList<>();
		for (Like like : likesList) {
			Optional<House> optionalHouse = houseService.findHouseById(like.getHouse().getId());
			housesList.add(optionalHouse.get());
		}
		model.addAttribute("housesList", housesList);

		return "likes/index";
	}

	// お気に入り追加
	@GetMapping("/{houseId}/create")
	public String create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PathVariable(name = "houseId") Integer houseId, RedirectAttributes redirectAttributes, Model model) {
		// ログイン中のユーザー情報を取得
		Integer userId = _getLoginUserDetail(userDetailsImpl);
		if (userId == 0) {
			redirectAttributes.addFlashAttribute("errorMessage", "ログインしてください。");
			return "redirect:/houses/" + houseId;
		}

		// 登録
		likeService.createLike(userId, houseId);
		redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");
		// お気に入り追加判定フラグの設定
		Boolean isLikeFlag = true;
		model.addAttribute("isLikeFlag", isLikeFlag);

		return "redirect:/houses/" + houseId;
	}

	// お気に入り解除
	@GetMapping("/{houseId}/delete")
	public String delete(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PathVariable(name = "houseId") Integer houseId, RedirectAttributes redirectAttributes, Model model) {
		// ログイン中のユーザー情報を取得
		Integer userId = _getLoginUserDetail(userDetailsImpl);
		if (userId == 0) {
			redirectAttributes.addFlashAttribute("errorMessage", "ログインしてください。");
			return "redirect:/houses/" + houseId;
		}

		// 削除
		likeService.deleteByUserIdAndHouseId(userId, houseId);
		redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
		// お気に入り解除判定フラグの設定
		Boolean isLikeFlag = false;
		model.addAttribute("isLikeFlag", isLikeFlag);

		return "redirect:/houses/" + houseId;
	}

	// ログイン中のユーザー情報を取得
	private Integer _getLoginUserDetail(UserDetailsImpl userDetailsImpl) {
		Integer userId = null;
		if (userDetailsImpl != null) {
			User user = userDetailsImpl.getUser();
			userId = user.getId();
			return userId;
		}
		return 0;
	}
}
