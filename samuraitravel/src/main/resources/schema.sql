-- 認可用テーブル生成
CREATE TABLE IF NOT EXISTS roles (
   id   INT         NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID'
  ,name VARCHAR(50) NOT NULL                            COMMENT 'ロール名'
);

-- 認証用テーブル生成
CREATE TABLE IF NOT EXISTS users (
   id            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID'
   ,name         VARCHAR(50)  NOT NULL                            COMMENT '氏名'
   ,furigana     VARCHAR(50)  NOT NULL                            COMMENT 'フリガナ'
   ,postal_code  VARCHAR(50)  NOT NULL                            COMMENT '郵便番号'
   ,address      VARCHAR(255) NOT NULL                            COMMENT '住所'
   ,phone_number VARCHAR(50)  NOT NULL                            COMMENT '電話番号'
   ,email        VARCHAR(255) NOT NULL                            COMMENT 'メールアドレス'
   ,password     VARCHAR(255) NOT NULL                            COMMENT 'パスワード'
   ,role_id      INT          NOT NULL                            COMMENT 'ロールID'
   ,enabled      BOOLEAN      NOT NULL                            COMMENT 'ユーザーの有効性'
   ,created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '作成日時'
   ,updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
   ,FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- メール認証用テーブル生成
CREATE TABLE IF NOT EXISTS verification_tokens (
   id         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID'
  ,user_id    INT          NOT NULL UNIQUE                     COMMENT 'ユーザーのID'
  ,token      VARCHAR(255) NOT NULL                            COMMENT 'トークン'
  ,created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '作成日時'
  ,updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
  ,FOREIGN KEY (user_id) REFERENCES users (id)
);

-- 民宿用テーブル生成
CREATE TABLE IF NOT EXISTS houses (
   id           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID'
  ,name         VARCHAR(50)  NOT NULL                            COMMENT '民宿名'
  ,image_name   VARCHAR(255)                                     COMMENT '民宿画像のファイル名'
  ,description  VARCHAR(255) NOT NULL                            COMMENT '民宿の説明'
  ,price        INT          NOT NULL                            COMMENT '一泊あたりの宿泊料金'
  ,capacity     INT          NOT NULL                            COMMENT '定員'
  ,postal_code  VARCHAR(50)  NOT NULL                            COMMENT '郵便番号'
  ,address      VARCHAR(255) NOT NULL                            COMMENT '住所'
  ,phone_number VARCHAR(50)  NOT NULL                            COMMENT '電話番号'
  ,created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '作成日時'
  ,updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
);

-- 民宿用テーブル生成
CREATE TABLE IF NOT EXISTS reservations (
   id                  INT  NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID'
  ,house_id            INT  NOT NULL                            COMMENT '民宿のID'
  ,user_id             INT  NOT NULL                            COMMENT 'ユーザーのID'
  ,checkin_date        DATE NOT NULL                            COMMENT 'チェックイン日'
  ,checkout_date       DATE NOT NULL                            COMMENT 'チェックアウト日'
  ,number_of_people    INT  NOT NULL                            COMMENT '宿泊人数'
  ,amount INT          NOT  NULL                                COMMENT '宿泊料金'
  ,created_at DATETIME NOT  NULL     DEFAULT CURRENT_TIMESTAMP  COMMENT '作成日時'
  ,updated_at DATETIME NOT  NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
  ,FOREIGN KEY (house_id) REFERENCES houses (id)
  ,FOREIGN KEY (user_id)  REFERENCES users (id)
 );

-- レビュー用テーブル生成
CREATE TABLE IF NOT EXISTS reviews (
   id                  INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID'
  ,house_id            INT          NOT NULL                            COMMENT '民宿のID'
  ,user_id             INT          NOT NULL                            COMMENT 'ユーザーのID'
  ,evaluation          VARCHAR(255) NOT NULL                            COMMENT '評価'
  ,detail              VARCHAR(255) NOT NULL                            COMMENT 'レビュー内容'
  ,created_at DATETIME NOT  NULL    DEFAULT CURRENT_TIMESTAMP           COMMENT '作成日時'
  ,updated_at DATETIME NOT  NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
  ,FOREIGN KEY (house_id) REFERENCES houses (id)
  ,FOREIGN KEY (user_id)  REFERENCES users (id)
 );

-- お気に入り用テーブル生成
CREATE TABLE IF NOT EXISTS likes (
   id                  INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'ID'
  ,house_id            INT          NOT NULL                            COMMENT '民宿のID'
  ,user_id             INT          NOT NULL                            COMMENT 'ユーザーのID'
  ,created_at DATETIME NOT  NULL    DEFAULT CURRENT_TIMESTAMP           COMMENT '作成日時'
  ,updated_at DATETIME NOT  NULL    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
  ,UNIQUE KEY (house_id, user_id)
  ,FOREIGN KEY (house_id) REFERENCES houses (id)
  ,FOREIGN KEY (user_id)  REFERENCES users (id)
 );


