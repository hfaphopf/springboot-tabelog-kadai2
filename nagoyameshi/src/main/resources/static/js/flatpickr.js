let maxDate = new Date();
 maxDate = maxDate.setMonth(maxDate.getMonth() + 3);
 
 flatpickr('#reservationDate', {
  mode: "single",
  locale: 'ja',
  minDate: 'today',
  maxDate: maxDate
});

flatpickr('#checkinTime', {
	locale: 'ja',
	enableTime: true,
	noCalendar: true,
	dateFormat: "H:i",
	time_24hr: true
});
