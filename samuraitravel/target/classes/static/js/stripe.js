const stripe = Stripe('pk_test_51QMUkXBz42A5tlf6MFjkNPBUqPUEySt39H73kD6ZkoIePDV03It1UwfgRgC5RsBBNvOhf0X42Ez0fm2bNbopkJaK006ShwZAhk');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});