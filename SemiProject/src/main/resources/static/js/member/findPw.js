// 인증 번호 받기 버튼 요소
const sendAuthkeyBtn = document.querySelector("#sendAuthKeyBtn");

// 이름 입력 input 요소
const memberName = document.querySelector("#memberName");

// 이메일(아이디) 입력 input 요소
const memberEmail = document.querySelector("#memberEmail");

// 인증번호 입력 input 요소
const authKey = document.querySelector("#authKey");

// 인증번호 입력 후 확인 버튼 요소
const checkAuthKeyBtn = document.querySelector("#checkAuthKeyBtn");

// 인증번호 관련 메시지 출력 span 요소
const authKeyMessage = document.querySelector("#authKeyMessage");

let authTimer; // 타이머 역할을 할 setInterval 함수를 저장할 변수

const initMin = 4; // 타이머 초기값(분)
const initSec = 59; // 타이머 초기값(초)
const initTime = "05:00";

// 클릭 시 타이머 숫자 초기화

const checkObj = {
  memberName: false,
  memberEmail: false,
  authKey: false,
};

min = initMin;
sec = initSec;

// 인증번호 받기 버튼 클릭시
sendAuthkeyBtn.addEventListener("click", () => {
  // 새로운 인증번호 발급을 원하는 것이기 때문에
  // 새로 발급 받은 인증번호 확인전까진 checkObj.authKey 는 false
  // 인증번호 발급 관련 메세지 지우기
  authKeyMessage.innerText = "";

  console.log(memberName.value);

  // 이름과 이메일을 입력하지 않았을 경우
  if (
    memberName.value.trim().length === 0 ||
    memberEmail.value.trim().length === 0
  ) {
    checkObj.memberName = false;
    checkObj.memberEmail = false;
    alert("이름과 이메일을 입력해주세요.");
    return;
  }

  const obj = {
    memberName: memberName.value,
    memberEmail: memberEmail.value,
  };

  fetch("/member/checkNM", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj),
  })
    .then((resp) => resp.text())
    .then((result) => {
      console.log(result);
      if (result == 1) {
        checkObj.memberName = true;
        checkObj.memberEmail = true;
        // 이전 동작중인 인터벌 클리어
        clearInterval(authTimer);

        //*******************************
        // 비동기로 서버에서 메일 보내기
        fetch("/email/signup", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: memberEmail.value,
        })
          .then((resp) => resp.text())
          .then((result) => {
            if (result == 1) {
              console.log("인증 번호 발송 성공");
            } else {
              console.log("인증 번호 발송 실패");
            }
          });

        // ******************************
        // 메일은 비동기로 서버에서 보내라고 놔두고
        // 화면에서는 타이머 시작하기
        authKeyMessage.innerText = initTime; // 05:00 세팅
        authKeyMessage.classList.remove("confirm", "error"); // 검정색 글씨

        alert("인증번호가 발송되었습니다.");

        // setInterval(콜백함수, 지연시간) : 지연시간(ms) 만큼 시간이 지날 때마다 함수를 실행

        // 인증 가능 시간 출력 (1초마다 동작)
        authTimer = setInterval(() => {
          authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;

          // 0분 0초인 경우
          if (min == 0 && sec == 0) {
            checkObj.authKey = false; // 인증 못함
            clearInterval(authTimer); // interval 멈춤
            authKeyMessage.classList.add("error");
            authKeyMessage.classList.remove("confirm");
            return;
          }

          // 0초인 경우 (0초를 출력한 후)
          if (sec == 0) {
            sec = 60;
            min--;
          } else {
            sec--;
          }
        }, 1000); // 1초 지연시간 마다 콜백함수 실행
      } else {
        checkObj.memberName = false;
        checkObj.memberEmail = false;
        alert("일치하는 회원 정보가 없습니다.");
        return;
      }
    });
});

// 매개변수 전달받은 숫자가 10미만인 경우 앞에 0붙여서 반환
function addZero(number) {
  if (number < 10) return "0" + number;
  else return number;
}

//------------------------------------------------

// 인증하기 버튼 클릭 시 입력된 인증번호를 비동기로 서버에 전달
// -> 입력된 인증번호화 발급된 인증번호가 같은지 비교
//    같으면 1, 다르면 0 반환
// 단, 타이머가 00:00초가 아닐 경우에만 수행
checkAuthKeyBtn.addEventListener("click", () => {
  if (min === 0 && sec === 0) {
    alert("인증번호 입력 제한 시간을 초과하였습니다. 다시 발급 받아 주세요");
    return;
  }

  if (authKey.value.length < 6 || authKey.value.length >= 7) {
    // 인증번호가 6자리가 아닌경우
    alert("인증번호를 정확히 입력해주세요");
    return;
  }

  // 문제 없는 경우
  // 입력 받은 이메일과 인증번호로 JS 객체 생성 후 전송
  const obj = {
    email: memberEmail.value,
    authKey: authKey.value,
  };

  // 인증번호 확인용 비동기 요청 보내기
  fetch("/email/checkAuthKey", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj), // obj라는 JS 객체를 JSON으로 변경
  })
    .then((resp) => resp.text())
    .then((result) => {
      // 1 or 0
      if (result == 0) {
        checkObj.authKey = false;
        alert("인증번호가 일치하지 않습니다");
        return;
      }

      // 인증번호가 일치할 경우
      clearInterval(authTimer); // 타이머 멈춤
      checkObj.authKey = true;
      authKeyMessage.innerText = "인증 되었습니다";
      authKeyMessage.classList.remove("error");
      authKeyMessage.classList.add("confirm");

      checkObj.authKey = true; // 인증번호 검사 여부 true
    });
});

const form = document.querySelector("form");

form.addEventListener("submit", (e) => {
  // checkObj의 저장된 값 중
  // 하나라도 false가 있으면 제출 X
  // for ~ of (반복가능한 배열 등에 사용되는 향상된 for문)
  // for ~in (객체 전용 향상된 for문)
  e.preventDefault();
  for (let key in checkObj) {
    // checkObj 요소의 key값을 순서대로 꺼내옴

    if (!checkObj[key]) {
      // 현재 접근 중인 key의 value 값이 false인 경우

      let str; // 출력할 메시지를 저장할 변수

      switch (key) {
        case "memberName":
          str = "이메일 인증번호를 발송하세요.";
          break;
        case "memberEmail":
          str = "이메일 인증번호를 발송하세요.";
          break;
        case "authKey":
          str = "이메일 인증번호 입력이 완료되지 않았습니다.";
          break;
      }
      console.log(checkObj);
      alert(str);

      document.getElementById(key).focus(); // 해당 input 초점 이동

      return;
    }
    form.submit();
  }
});
