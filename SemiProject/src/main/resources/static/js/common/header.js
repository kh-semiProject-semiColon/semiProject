// 공통 작동 js -------------------------------------
// 사이드바 메뉴 활성화
// location.pathname는 /myPage/info를 다운받습니다
document.addEventListener("DOMContentLoaded", function () {
  const currentLocation = location.pathname; // 현재 페이지 url

  const announce = document.querySelector("#announce");
  const hire = document.querySelector("#hire");
  const board1 = document.querySelector("#board1");
  const studyNow = document.querySelector("#studyNow");
  const board2 = document.querySelector("#board2");
  const studyBoard = document.querySelector("#studyBoard");

  switch (currentLocation) {
    case "/board/announce":
      announce.classList.add("active");
      hire.classList.remove("active");
      board1.classList.remove("active");
      studyNow.classList.remove("active");
      board2.classList.remove("active");
      studyBoard.classList.remove("active");
      break;

    case "/hire/board":
      announce.classList.remove("active");
      hire.classList.add("active");
      board1.classList.remove("active");
      studyNow.classList.remove("active");
      board2.classList.remove("active");
      studyBoard.classList.remove("active");
      break;

    case "/board/1":
      announce.classList.remove("active");
      hire.classList.remove("active");
      board1.classList.add("active");
      studyNow.classList.remove("active");
      board2.classList.remove("active");
      studyBoard.classList.remove("active");
      break;

    case "/study/studyNow":
      announce.classList.remove("active");
      hire.classList.remove("active");
      board1.classList.remove("active");
      studyNow.classList.add("active");
      board2.classList.remove("active");
      studyBoard.classList.remove("active");
      break;

    case "/board/2":
      announce.classList.remove("active");
      hire.classList.remove("active");
      board1.classList.remove("active");
      studyNow.classList.remove("active");
      board2.classList.add("active");
      studyBoard.classList.remove("active");
      break;

    case "/studyBoard/calendar":
      announce.classList.remove("active");
      hire.classList.remove("active");
      board1.classList.remove("active");
      studyNow.classList.remove("active");
      board2.classList.remove("active");
      studyBoard.classList.add("active");
      break;
  }
});
