const stripe = Stripe('pk_test_51OokMvDYS08B5lADS6zVg7Cc0DxATiJfNEUujA4qTk7g9pZLo3zuCx9rGriYGI5Kyyzf9djSsLUbvUsL3wkBTjOu00nOpNv0CV');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
  stripe.redirectToCheckout({
    sessionId: sessionId
  })
});
