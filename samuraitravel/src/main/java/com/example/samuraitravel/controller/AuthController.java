package com.example.samuraitravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.entity.VerificationToken;
import com.example.samuraitravel.event.SignupEventPublisher;
import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.service.UserService;
import com.example.samuraitravel.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 認証系コントローラー
 */
@Controller
public class AuthController {
  private final UserService userService;
  private final SignupEventPublisher signupEventPublisher;
  private final VerificationTokenService verificationTokenService;

  // コンストラクター
  public AuthController(UserService userService, SignupEventPublisher signupEventPublisher,
      VerificationTokenService verificationTokenService) {
    this.userService = userService;
    this.signupEventPublisher = signupEventPublisher;
    this.verificationTokenService = verificationTokenService;
  }

  // ログイン
  @GetMapping("/login")
  public String login() {
    return "auth/login";
  }

  // 会員登録
  @GetMapping("/signup")
  public String signup(Model model) {
    model.addAttribute("signupForm", new SignupForm());
    return "auth/signup";
  }

  @PostMapping("/signup")
  public String signup(@ModelAttribute @Validated SignupForm signupForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes,
      HttpServletRequest httpServletRequest,
      Model model) {
    // メールアドレス登録済みチェック
    if (userService.isEmailRegistered(signupForm.getEmail())) {
      // 登録済みエラー
      FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
      bindingResult.addError(fieldError);
    }

    // エラー情報がある場合
    if (bindingResult.hasErrors()) {
      model.addAttribute("signupForm", signupForm);
      return "auth/signup";
    }

    // データベース登録
    User createdUser = userService.createUser(signupForm);
    String requestUrl = new String(httpServletRequest.getRequestURL());
    signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
    redirectAttributes.addFlashAttribute("successMessage",
        "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");
    return "redirect:/";
  }

  // メール認証用URL
  @GetMapping("/signup/verify")
  public String verify(@RequestParam(name = "token") String token, Model model) {
    VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

    // verification_tokensテーブルから一致するトークンを探す
    if (verificationToken != null) {
      User user = verificationToken.getUser();
      userService.enableUser(user);
      String successMessage = "会員登録が完了しました。";
      model.addAttribute("successMessage", successMessage);
    } else {
      String errorMessage = "トークンが無効です。";
      model.addAttribute("errorMessage", errorMessage);
    }
    return "auth/verify";
  }
}
