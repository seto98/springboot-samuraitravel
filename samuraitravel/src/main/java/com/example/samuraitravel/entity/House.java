package com.example.samuraitravel.entity;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

/*
 * 民宿用Entity
 * TABLE：samuraitravel_db.houses
 */
@Entity
@Table(name = "houses")
@Data
@ToString(exclude = "reservations")
public class House {
	/** ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/** 民宿名 */
	@Column(name = "name")
	private String name;

	/** 民宿画像のファイル名 */
	@Column(name = "image_name")
	private String imageName;

	/** 民宿の説明 */
	@Column(name = "description")
	private String description;

	/** 一泊あたりの宿泊料金 */
	@Column(name = "price")
	private Integer price;

	/** 定員 */
	@Column(name = "capacity")
	private Integer capacity;

	/** 郵便番号 */
	@Column(name = "postal_code")
	private String postalCode;

	/** 住所 */
	@Column(name = "address")
	private String address;

	/** 電話番号 */
	@Column(name = "phone_number")
	private String phoneNumber;

	/** 作成日時 */
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;

	/** 更新日時 */
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;

	@OneToMany(mappedBy = "house", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Reservation> reservations;
	
	@OneToMany(mappedBy = "house", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Review> reviews;


}
