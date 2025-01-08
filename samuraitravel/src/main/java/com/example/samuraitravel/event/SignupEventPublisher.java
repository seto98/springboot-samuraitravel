package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;

/**
 * イベント発生用クラス
 */
@Component
public class SignupEventPublisher {
  private final ApplicationEventPublisher applicationEventPublisher;

  // コンストラクター
  public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  // メール認証イベント呼び出し
  public void publishSignupEvent(User user, String requestUrl) {
    applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
  }
}
