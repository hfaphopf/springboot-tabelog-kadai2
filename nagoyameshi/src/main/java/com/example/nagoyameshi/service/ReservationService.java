package com.example.nagoyameshi.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class ReservationService {
	// 予約情報をデータベースに登録
	private final ReservationRepository reservationRepository;
	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;

	public ReservationService(ReservationRepository reservationRepository, RestaurantRepository restauranteRepository,
			UserRepository userRepository) {
		this.reservationRepository = reservationRepository;
		this.restaurantRepository = restauranteRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public void create(ReservationRegisterForm reservationRegisterForm) {
		Reservation reservation = new Reservation();
		Restaurant restaurant = restaurantRepository.getReferenceById(reservationRegisterForm.getRestaurantId());
		User user = userRepository.getReferenceById(reservationRegisterForm.getUserId());
		LocalDate reservationDate = LocalDate.parse(reservationRegisterForm.getReservationDate());
		LocalTime reservationTime = LocalTime.parse(reservationRegisterForm.getReservationTime());

		reservation.setRestaurant(restaurant);
		reservation.setUser(user);
		reservation.setReservationDate(reservationDate);
		reservation.setReservationTime(reservationTime);
		reservation.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople());

		reservationRepository.save(reservation);
	}

	// 宿泊人数が定員以下かどうかをチェックする
	public boolean isWithinSeats(Integer numberOfPeople, Integer seats) {
		return numberOfPeople <= seats;
	}

	public void save(@Valid ReservationInputForm form) {
		
	}
}
