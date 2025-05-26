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
  } else {
    currentIndex = 0; // 마지막 슬라이드면 처음으로
  }
  updateSlide();
}

function prevSlide() {
  if (currentIndex > 0) {
    currentIndex--;
    updateSlide();
  }
}

// 자동으로 다음 슬라이드로 이동
setInterval(nextSlide, 4000);
