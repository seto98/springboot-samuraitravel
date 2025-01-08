package com.example.samuraitravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.VerificationToken;

/**
 * メール認証用リポジトリ
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
  public VerificationToken findByToken(String token);
}
