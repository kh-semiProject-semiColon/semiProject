let currentIndex = 0;
let intervalId = null;

function updateSlide() {
  const carousel = document.querySelector(".carousel");
  const slideWidth = document
    .querySelector(".study-status-card")
    .getBoundingClientRect().width;
  carousel.style.transform = `translateX(-${currentIndex * slideWidth}px)`;
}

function nextSlide() {
  const totalSlides = document.querySelectorAll(".study-status-card").length;
  currentIndex = (currentIndex + 1) % totalSlides; // 마지막이면 처음으로 순환
  updateSlide();
}

function prevSlide() {
  const totalSlides = document.querySelectorAll(".study-status-card").length;
  currentIndex = (currentIndex - 1 + totalSlides) % totalSlides;
  updateSlide();
}

// 자동 슬라이드 시작
function startAutoSlide() {
  intervalId = setInterval(nextSlide, 4000); // 4초마다 다음 슬라이드
}

// 자동 슬라이드 멈춤
function stopAutoSlide() {
  clearInterval(intervalId);
}

// 마우스 호버 감지
const carouselContainer = document.querySelector(".carousel-container");

carouselContainer.addEventListener("mouseenter", stopAutoSlide);
carouselContainer.addEventListener("mouseleave", startAutoSlide);

// 초기 실행
startAutoSlide();
