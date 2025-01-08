package com.example.samuraitravel.entity;

import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

/*
 * メール認証用Entity
 * TABLE：samuraitravel_db.verification_tokens
 */
@Entity
@Table(name = "verification_tokens")
@Data
public class VerificationToken {
  /** ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  /** ユーザーのID */
  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  /** トークン */
  @Column(name = "token")
  private String token;

  /** 作成日時 */
  @Column(name = "created_at", insertable = false, updatable = false)
  private Timestamp createdAt;

  /** 更新日時 */
  @Column(name = "updated_at", insertable = false, updatable = false)
  private Timestamp updatedAt;
}
