// 사이드바 hover 옵션 설정정
document.querySelectorAll(".sidebar-menu-item").forEach((item) => {
  item.addEventListener("click", function () {
    document.querySelectorAll(".sidebar-menu-item").forEach((i) => {
      i.classList.remove("active");
    });
    this.classList.add("active");
  });
});

const submitBtn = document.querySelector(".submit-btn");

// 다음으로 버튼
submitBtn.addEventListener("click", () => {
  location.href = "/";
});
