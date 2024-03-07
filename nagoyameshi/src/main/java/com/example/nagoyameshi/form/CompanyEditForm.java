package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyEditForm {
	@NotBlank(message = "会社名を入力してください。")
	private String companyName;

	@NotBlank(message = "所在地を入力してください。")
	private String location;

	@NotBlank(message = "代表者名を入力してください。")
	private String representativeName;

	@NotBlank(message = "設立日を入力してください。")
	private String establishmentDate;

	@NotBlank(message = "資本金を入力してください。")
	private String capital;

	@NotBlank(message = "事業内容を入力してください。")
	private String businessContent;
}
