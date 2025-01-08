package com.example.samuraitravel.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 認証用Entity
 * TABLE:samuraitravel_db.users
 */
@Entity
@Table(name = "users")
@Data
public class User {
  /** ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  /** 氏名 */
  @Column(name = "name")
  private String name;

  /** フリガナ */
  @Column(name = "furigana")
  private String furigana;

  /** 郵便番号 */
  @Column(name = "postal_code")
  private String postalCode;

  /** 住所 */
  @Column(name = "address")
  private String address;

  /** 電話番号 */
  @Column(name = "phone_number")
  private String phoneNumber;

  /** メールアドレス */
  @Column(name = "email")
  private String email;

  /** パスワード */
  @Column(name = "password")
  private String password;

  /** ロールID */
  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  /** ユーザーの有効性 */
  @Column(name = "enabled")
  private Boolean enabled;

  /** 作成日時 */
  @Column(name = "created_at", insertable = false, updatable = false)
  private Timestamp createdAt;

  /** 更新日時 */
  @Column(name = "updated_at", insertable = false, updatable = false)
  private Timestamp updatedAt;
}
