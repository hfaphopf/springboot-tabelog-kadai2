package com.example.nagoyameshi.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
	@NotBlank(message = "予約日を選択してください。")
	private String reservationDate;

	@NotNull(message = "人数を入力してください。")
	@Min(value = 1, message = "人数は1人以上に設定してください。")
	private Integer numberOfPeople;

	// 予約日を取得する

	//フィールド
	private LocalDate checkinDate;

	//ゲッター
	public LocalDate getCheckinDate() {
		//        String checkReservationDate = getReservationDate();
		//        return LocalDate.parse(checkReservationDate);
		return this.checkinDate;
	}

	//セッター
	public void setCheckinDate(LocalDate checkinDate) {
		this.checkinDate = checkinDate;
	}
}