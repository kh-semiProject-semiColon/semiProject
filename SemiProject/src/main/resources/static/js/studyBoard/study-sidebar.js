// 사이드바 메뉴 활성화
// location.pathname는 /myPage/info를 다운받습니다
document.addEventListener("DOMContentLoaded", function () {
  const currentLocation = location.pathname; // 현재 페이지 url

  const calendar = document.querySelector("#studyBoard-calendar");
  const rulecontent = document.querySelector("#studyBoard-rulecontent");
  const studyBoard = document.querySelector("#studyBoard-studyBoard");
  const update = document.querySelector("#studyBoard-update");
  const delete1 = document.querySelector("#studyBoard-delete");
  const chatting = document.querySelector("#studyBoard-chatting");

  switch (currentLocation) {
    case "/studyBoard/calendar":
      calendar.classList.add("active");
      rulecontent.classList.remove("active");
      studyBoard.classList.remove("active");
      update.classList.remove("active");
      delete1.classList.remove("active");
      chatting.classList.remove("active");
      break;

    case "/studyBoard/rulecontent":
      calendar.classList.remove("active");
      rulecontent.classList.add("active");
      studyBoard.classList.remove("active");
      update.classList.remove("active");
      delete1.classList.remove("active");
      chatting.classList.remove("active");
      break;

    case "/studyBoard/studyBoard":
      calendar.classList.remove("active");
      rulecontent.classList.remove("active");
      studyBoard.classList.add("active");
      update.classList.remove("active");
      delete1.classList.remove("active");
      chatting.classList.remove("active");
      break;

    case "/studyBoard/update":
      calendar.classList.remove("active");
      rulecontent.classList.remove("active");
      studyBoard.classList.remove("active");
      update.classList.add("active");
      delete1.classList.remove("active");
      chatting.classList.remove("active");
      break;

    case "/studyBoard/delete":
      calendar.classList.remove("active");
      rulecontent.classList.remove("active");
      studyBoard.classList.remove("active");
      update.classList.remove("active");
      delete1.classList.add("active");
      chatting.classList.remove("active");
      break;

    case "/studyBoard/chatting":
      calendar.classList.remove("active");
      rulecontent.classList.remove("active");
      studyBoard.classList.remove("active");
      update.classList.remove("active");
      delete1.classList.remove("active");
      chatting.classList.add("active");
      break;
  }
});
