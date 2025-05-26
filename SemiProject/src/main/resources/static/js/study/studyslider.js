let currentIndex = 0;

function updateSlide() {
  const carousel = document.querySelector(".carousel");
  const slideWidth = document
    .querySelector(".study-status-card")
    .getBoundingClientRect().width;
  carousel.style.transform = `translateX(-${currentIndex * slideWidth}px)`;
}

function nextSlide() {
  const totalSlides = document.querySelectorAll(".study-status-card").length;
  if (currentIndex < totalSlides - 1) {
    currentIndex++;
    updateSlide();
  }
}

function prevSlide() {
  if (currentIndex > 0) {
    currentIndex--;
    updateSlide();
  }
}
