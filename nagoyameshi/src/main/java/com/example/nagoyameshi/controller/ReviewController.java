package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReviewService;

//restaurants/{restaurantId}/reviewsへのルートパスを指定
@Controller
@RequestMapping("/restaurants/{restaurantId}/reviews")
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final RestaurantRepository restaurantRepository;
	private final ReviewService reviewService;

	public ReviewController(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository,
			ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.restaurantRepository = restaurantRepository;
		this.reviewService = reviewService;
	}

	//restaurantIdを取得してページ設定を指定する
	@GetMapping
	public String index(@PathVariable(name = "restaurantId") Integer restaurantId,
			@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);

		//検索時、投稿が古い順にレビューを並び替え
		Page<Review> reviewPage = reviewRepository.findByRestaurantOrderByCreatedAtDesc(restaurant, pageable);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("reviewPage", reviewPage);

		return "reviews/index";
	}

	//レビューを投稿する
	@GetMapping("/register")
	public String register(@PathVariable(name = "restaurantId") Integer restaurantId, Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());

		return "reviews/register";
	}

	@PostMapping("/create")
	public String create(@PathVariable(name = "restaurantId") Integer restaurantId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
		User user = userDetailsImpl.getUser();

		//投稿失敗時、reviews/registerに遷移
		if (bindingResult.hasErrors()) {
			model.addAttribute("restaurant", restaurant);
			return "reviews/register";
		}

		//投稿成功時、下記メッセージを表示
		reviewService.create(restaurant, user, reviewRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");

		return "redirect:/restaurants/{restaurantId}";
	}

	//投稿したレビューを更新する
	//reviews/editを表示
	@GetMapping("/{reviewId}/edit")
	public String edit(@PathVariable(name = "restaurantId") Integer restaurantId,
			@PathVariable(name = "reviewId") Integer reviewId, Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
		Review review = reviewRepository.getReferenceById(reviewId);

		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getScore(), review.getContent());

		model.addAttribute("restaurant", restaurant);
		model.addAttribute("review", review);
		model.addAttribute("reviewEditForm", reviewEditForm);

		return "reviews/edit";
	}

	//レビュー内容を更新する
	@PostMapping("/{reviewId}/update")
	public String update(@PathVariable(name = "restaurantId") Integer restaurantId,
			@PathVariable(name = "reviewId") Integer reviewId,
			@ModelAttribute @Validated ReviewEditForm reviewEditForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
		Review review = reviewRepository.getReferenceById(reviewId);

		//投稿失敗時、reviews/editに遷移
		if (bindingResult.hasErrors()) {
			model.addAttribute("restaurant", restaurant);
			model.addAttribute("review", review);
			return "reviews/edit";
		}

		//投稿成功時、下記メッセージを表示
		reviewService.update(reviewEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");

		return "redirect:/restaurants/{restaurantId}";
	}

	//レビュー内容を削除する
	@PostMapping("/{reviewId}/delete")
	public String delete(@PathVariable(name = "reviewId") Integer reviewId, RedirectAttributes redirectAttributes) {
		reviewRepository.deleteById(reviewId);

		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");

		return "redirect:/restaurants/{restaurantId}";
	}
}