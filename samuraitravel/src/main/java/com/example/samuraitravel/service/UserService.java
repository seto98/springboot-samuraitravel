package com.example.samuraitravel.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Role;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.SignupForm;
import com.example.samuraitravel.form.UserEditForm;
import com.example.samuraitravel.repository.RoleRepository;
import com.example.samuraitravel.repository.UserRepository;

/**
 * 会員登録機能サービスクラス
 */
@Service
public class UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  // コントラクター
  public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * フォームから送信された会員情報をデータベースへ登録
   * 
   * @param signupForm 入力フォーム
   * @return 認証用情報
   */
  @Transactional
  public User createUser(SignupForm signupForm) {
    User user = new User();
    Role role = roleRepository.findByName("ROLE_GENERAL");

    // フォームに入力された値をEntityに詰めなおし
    user.setName(signupForm.getName());
    user.setFurigana(signupForm.getFurigana());
    user.setPostalCode(signupForm.getPostalCode());
    user.setAddress(signupForm.getAddress());
    user.setPhoneNumber(signupForm.getPhoneNumber());
    user.setEmail(signupForm.getEmail());
    user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
    user.setRole(role);
    user.setEnabled(false);

    // データベースへ保存
    return userRepository.save(user);
  }
  
  @Transactional
  public void updateUser(UserEditForm userEditForm, User user) {
	  user.setName(userEditForm.getName());
	  user.setFurigana(userEditForm.getFurigana());
	  user.setPostalCode(userEditForm.getPostalCode());
	  user.setAddress(userEditForm.getAddress());
	  user.setPhoneNumber(userEditForm.getPhoneNumber());
	  user.setEmail(userEditForm.getEmail());
	  userRepository.save(user);
  }

  /**
   * メールアドレス登録済みチェック
   * 
   * @param email メールアドレス
   * @return チェック結果
   */
  public boolean isEmailRegistered(String email) {
    User user = userRepository.findByEmail(email);
    return user != null;
  }

  /**
   * パスワード同一チェック
   * 
   * @param password             パスワード
   * @param passwordConfirmation パスワード（確認用）
   * @return チェック結果
   */
  public boolean isSamePassword(String password, String passwordConfirmation) {
    return password.equals(passwordConfirmation);
  }

  /**
   * ユーザーを有効にする
   * @param user  ユーザー情報
   */
  @Transactional
  public void enableUser(User user) {
    user.setEnabled(true);
    userRepository.save(user);
  }
  
  // メールアドレスが変更されたかどうかをチェックする
  public boolean isEmailChanged(UserEditForm userEditForm, User user) {
	  return !userEditForm.getEmail().equals(user.getEmail());
  }
  
  // 指定したメールアドレスを持つユーザーを取得する
  public User findUserByEmail(String email) {
	  return userRepository.findByEmail(email);
  }
  
  // すべてのユーザーをページングされた状態で取得する
  public Page<User> findAllUsers(Pageable pageable) {
	  return userRepository.findAll(pageable);
  }
  
  // 指定されたキーワードを氏名またはフリガナに含むユーザーを、ページングされた状態で取得する
  public Page<User> findUsersByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKetword, Pageable pageable) {
	  return userRepository.findByNameLikeOrFuriganaLike("%" + nameKeyword + "%", "%" + furiganaKetword + "%", pageable);
  }
  
  // 指定したidを持つユーザーを取得する
  public Optional<User> findUserById(Integer id) {
	  return userRepository.findById(id);
  }
}
