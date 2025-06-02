document.addEventListener("DOMContentLoaded", function () {
  const currentLocation = location.pathname;

  const announce = document.querySelector("#announce");
  const hire = document.querySelector("#hire");
  const board1 = document.querySelector("#board1");
  const studyNow = document.querySelector("#studyNow");
  const board2 = document.querySelector("#board2");
  const studyBoard = document.querySelector("#studyBoard");

  console.log("현재 경로:", currentLocation);

  if (currentLocation.startsWith("/board/announce")) {
    announce.classList.add("active");
  } else if (currentLocation.startsWith("/hire/board")) {
    hire.classList.add("active");
  } else if (currentLocation.startsWith("/board/1")) {
    board1.classList.add("active");
  } else if (currentLocation.startsWith("/study/studyNow")) {
    studyNow.classList.add("active");
  } else if (
    currentLocation.startsWith("/board/2") ||
    currentLocation.startsWith("/editBoard/2")
  ) {
    board2.classList.add("active");
  } else if (currentLocation.startsWith("/studyBoard/")) {
    studyBoard.classList.add("active");
  }
});
