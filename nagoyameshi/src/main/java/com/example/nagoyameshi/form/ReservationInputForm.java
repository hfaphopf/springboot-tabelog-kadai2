package com.example.nagoyameshi.form;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
	@NotBlank(message = "予約日を選択してください。")
	private String reservationDate;

	@NotBlank(message = "予約時間を選択してください。")
    private String reservationTime;
	
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
	

	// 予約時間を取得する
	//フィールド
		private LocalTime checkinTime;

		//ゲッター
		public LocalTime getCheckinTime() {
			//        String checkReservationTime = getReservationTime();
			//        return LocalTime.parse(checkReservationTime);
			return this.checkinTime;
		}

		//セッター
		public void setCheckinTime(LocalTime checkinTime) {
			this.checkinTime = checkinTime;
		}
}
