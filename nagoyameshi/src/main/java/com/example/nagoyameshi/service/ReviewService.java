package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;
import com.example.nagoyameshi.repository.ReviewRepository;

//ReviewRepositoryに取得したidを渡す
@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;

	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}

	//Idを取得してレビューを新規投稿する
	@Transactional
	public void create(Restaurant restaurant, User user, ReviewRegisterForm reviewRegisterForm) {
		Review review = new Review();

		review.setRestaurant(restaurant);
		review.setUser(user);
		review.setScore(reviewRegisterForm.getScore());
		review.setContent(reviewRegisterForm.getContent());

		reviewRepository.save(review);
	}

	//Idを取得してレビュー内容を更新する
	@Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());

		review.setScore(reviewEditForm.getScore());
		review.setContent(reviewEditForm.getContent());

		reviewRepository.save(review);
	}

	public boolean hasUserAlreadyReviewed(Restaurant restaurant, User user) {
		return reviewRepository.findByRestaurantAndUser(restaurant, user) != null;
	}
}