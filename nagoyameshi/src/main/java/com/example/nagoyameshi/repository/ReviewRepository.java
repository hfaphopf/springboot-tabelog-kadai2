package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;

//参考にしたQA:https://terakoya.sejuku.net/question/detail/33847

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	//投稿されたレビューを投稿された順に並べて、最新の6件のレビューを取得する。
	public List<Review> findTop6ByRestaurantOrderByCreatedAtDesc(Restaurant restaurant);

	//指定された店舗とユーザーに関連するレビューを取得
	public Review findByRestaurantAndUser(Restaurant restaurant, User user);

	//指定された店舗に関連するレビューの数を数える
	public long countByRestaurant(Restaurant restaurant);

	//検索時、投稿が古い順にレビューを並び替え
	public Page<Review> findByRestaurantOrderByCreatedAtDesc(Restaurant restaurant, Pageable pageable);
}