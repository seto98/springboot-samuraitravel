package com.example.samuraitravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Role;

/**
 * 認可用リポジトリ
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
  /**
   * ロール名でロールを検索
   * 
   * @param name ロール名
   * @return ロール
   */
  public Role findByName(String name);
}
