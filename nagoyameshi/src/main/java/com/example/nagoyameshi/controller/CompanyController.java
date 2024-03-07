package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyController {

	@GetMapping("/company")
	public String company() {
		return "admin/company/CompanyPage";
	}
}
