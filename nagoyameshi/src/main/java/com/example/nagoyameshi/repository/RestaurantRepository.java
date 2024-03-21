package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Restaurant;


public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);

	//店舗名または目的地で検索する（新着順）
	public Page<Restaurant> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword,
			Pageable pageable);

	//店舗名または目的地で検索する（料金が安い順）
	public Page<Restaurant> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword,
			Pageable pageable);

	//カテゴリで検索する（新着順）
	public Page<Restaurant> findByCategoryLikeOrderByCreatedAtDesc(String category, Pageable pageable);

	//カテゴリで検索する（料金が安い順）
	public Page<Restaurant> findByCategoryLikeOrderByPriceAsc(String category, Pageable pageable);

	public Page<Restaurant> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);

	public Page<Restaurant> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);

	//すべてのデータを取得する（新着順）
	public Page<Restaurant> findAllByOrderByCreatedAtDesc(Pageable pageable);

	//すべてのデータを取得する（料金が安い順）
	public Page<Restaurant> findAllByOrderByPriceAsc(Pageable pageable);

	public List<Restaurant> findTop10ByOrderByCreatedAtDesc();
}
