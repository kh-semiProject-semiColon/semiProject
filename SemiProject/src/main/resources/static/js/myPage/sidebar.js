// 공통 작동 js -------------------------------------
// 사이드바 메뉴 활성화
// location.pathname는 /myPage/info를 다운받습니다
document.addEventListener("DOMContentLoaded", function () {
  const currentLocation = location.pathname; // 현재 페이지 url

  const info = document.querySelector("#myPage-info");
  const posts = document.querySelector("#myPage-posts");
  const change = document.querySelector("#myPage-change");
  const delete1 = document.querySelector("#myPage-delete");

  switch (currentLocation) {
    case "/myPage/info":
      info.classList.add("active");
      posts.classList.remove("active");
      change.classList.remove("active");
      delete1.classList.remove("active");
      break;

    case "/myPage/posts":
      info.classList.remove("active");
      posts.classList.add("active");
      change.classList.remove("active");
      delete1.classList.remove("active");
      break;

    case "/myPage/changePw":
      info.classList.remove("active");
      posts.classList.remove("active");
      change.classList.add("active");
      delete1.classList.remove("active");
      break;

    case "/myPage/delete1":
    case "/myPage/delete2":
    case "/myPage/delete3":
      info.classList.remove("active");
      posts.classList.remove("active");
      change.classList.remove("active");
      delete1.classList.add("active");
      break;
  }
});
