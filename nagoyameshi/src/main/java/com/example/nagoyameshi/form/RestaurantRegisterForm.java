package com.example.nagoyameshi.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantRegisterForm {
	@NotBlank(message = "店舗名を入力してください。")
	private String name;

	private MultipartFile imageFile;

	@NotBlank(message = "説明を入力してください。")
	private String description;

	@NotNull(message = "価格帯を入力してください。")
	@Min(value = 100, message = "価格帯は100円以上に設定してください。")
	private Integer price;

	@NotBlank(message = "郵便番号を入力してください。")
	private String postalCode;

	@NotBlank(message = "住所を入力してください。")
	private String address;

	@NotBlank(message = "電話番号を入力してください。")
	private String phoneNumber;

	@NotBlank(message = "営業時間を入力してください。")
	private String businessHours;

	@NotBlank(message = "定休日を入力してください。")
	private String regularHoliday;

	@NotNull(message = "座席数を入力してください。")
	private Integer seats;

	@NotBlank(message = "カテゴリを入力してください。")
	private String category;
}
