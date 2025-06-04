// 페이지네이션 활성화
document
  .querySelectorAll(".pagination .page-item:not(.arrow)")
  .forEach((item) => {
    item.addEventListener("click", function () {
      document.querySelectorAll(".pagination .page-item").forEach((i) => {
        i.classList.remove("active");
      });
      this.classList.add("active");
    });
  });

// 모바일 메뉴 토글 기능 추가
const menuIcon = document.querySelector(".menu-icon");

if (menuIcon) {
  menuIcon.addEventListener("click", function () {
    const navMenu = document.querySelector(".nav-menu");
    navMenu.style.display = navMenu.style.display === "flex" ? "none" : "flex";

    if (navMenu.style.display === "flex") {
      navMenu.style.position = "absolute";
      navMenu.style.top = "70px";
      navMenu.style.left = "0";
      navMenu.style.width = "100%";
      navMenu.style.flexDirection = "column";
      navMenu.style.backgroundColor = "#fff";
      navMenu.style.boxShadow = "0 2px 5px rgba(0,0,0,0.1)";
      navMenu.style.zIndex = "100";

      document.querySelectorAll(".nav-menu li").forEach((item) => {
        item.style.margin = "0";
        item.style.width = "100%";
        item.style.textAlign = "center";
        item.style.borderBottom = "1px solid #DBDAE2";
      });
    }
  });
}
// 반응형 화면 크기 변경 감지
window.addEventListener("resize", function () {
  const navMenu = document.querySelector(".nav-menu");
  if (navMenu) {
    if (window.innerWidth > 992) {
      navMenu.style.display = "flex";
      navMenu.style.position = "absolute";
      navMenu.style.top = "auto";
      // navMenu.style.left = "50%";
      navMenu.style.width = "auto";
      navMenu.style.flexDirection = "row";
      navMenu.style.backgroundColor = "transparent";
      navMenu.style.boxShadow = "none";

      document.querySelectorAll(".nav-menu li").forEach((item) => {
        item.style.margin = "0 15px";
        item.style.width = "auto";
        item.style.textAlign = "center";
        item.style.borderBottom = "none";
      });
    } else {
      navMenu.style.display = "none";
    }
  }
});
