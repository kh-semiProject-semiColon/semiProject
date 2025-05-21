// 프로필 조회 js --------------------------------------------------
document.addEventListener("DOMContentLoaded", function () {
  const removeImageBtn = document.querySelector(".remove-image");
  const profileImage = document.querySelector(".profile-image");
  const imageSelectBtn = document.querySelector(".image-select-btn");
  const submitBtn = document.querySelector(".submit-btn");

  // 이미지 제거 기능
  removeImageBtn.addEventListener("click", function () {
    profileImage.src = "/images/default-profile.png";
    // 실제 구현 시에는 hidden input을 활용하여 이미지 제거 상태를 서버에 전송할 수 있음
  });

  // 이미지 선택 버튼 기능
  imageSelectBtn.addEventListener("click", function () {
    const fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.accept = "image/*";
    fileInput.click();

    fileInput.addEventListener("change", function () {
      if (this.files && this.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
          profileImage.src = e.target.result;
        };
        reader.readAsDataURL(this.files[0]);
      }
    });
  });

  // 주소 검색 버튼 기능
  const addressSearchBtn = document.querySelector(".address-search");
  addressSearchBtn.addEventListener("click", function () {
    // 주소 검색 API 연동 코드 (실제 구현 필요)
    alert("주소 검색 기능이 연동됩니다.");
  });

  // 사이드바 메뉴 활성화
  document.querySelectorAll(".sidebar-menu-item").forEach((item) => {
    item.addEventListener("click", function () {
      document.querySelectorAll(".sidebar-menu-item").forEach((i) => {
        i.classList.remove("active");
      });
      this.classList.add("active");
    });
  });

  // 반응형 메뉴 토글
  const menuButton = document.querySelector(".menu-button");
  const navMenu = document.querySelector(".nav-menu");

  menuButton.addEventListener("click", function () {
    navMenu.style.display = navMenu.style.display === "flex" ? "none" : "flex";
  });
});
