// 비밀번호 / 비밀번호 확인 유효성 검사

// 1) 비밀번호 관련 요소 얻어오기
const memberPw = document.querySelector("#memberPw");
const memberPwConfirm = document.querySelector("#memberPwConfirm");
const pwMessage = document.querySelector("#pwMessage");
const confirmPwMessage = document.querySelector("#confirmPwMessage");

const checkObj = {
  memberPw: false,
  memberPwConfirm: false,
};

// 5) 비밀번호, 비밀번호 확인이 같은지 검사하는 함수
const checkPw = () => {
  console.log("d응");

  // 같은 경우
  if (memberPw.value === memberPwConfirm.value) {
    confirmPwMessage.innerText = "비밀번호가 일치합니다";
    confirmPwMessage.classList.add("confirm");
    confirmPwMessage.classList.remove("error");
    checkObj.memberPwConfirm = true;
    return;
  }

  // 다를 경우
  confirmPwMessage.innerText = "비밀번호가 일치하지 않습니다";
  confirmPwMessage.classList.remove("confirm");
  confirmPwMessage.classList.add("error");
  checkObj.memberPwConfirm = false;
};

// 2) 비밀번호 유효성 검사
memberPw.addEventListener("input", (e) => {
  console.log(2);
  // 입력 받은 비밀번호 값
  const inputPw = e.target.value;

  // 3) 입력되지 않은 경우
  if (inputPw.trim().length === 0) {
    pwMessage.innerText =
      "영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요.";
    pwMessage.classList.remove("confirm", "error");
    checkObj.memberPw = false; // 비밀번호가 유효하지 않다고 표시
    memberPw.value = ""; // 첫글자 띄어쓰지 입력 못하게 막기
    return;
  }

  // 4) 입력 받은 비밀번호 정규식 검사

  const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;

  if (!regExp.test(inputPw)) {
    // 유효하지 않으면
    pwMessage.innerText = "유효한 비밀번호를 입력하세요";
    pwMessage.classList.add("error");
    pwMessage.classList.remove("confirm");
    checkObj.memberPw = false;
    return;
  }

  // 유효한 경우
  pwMessage.innerText = "유효한 비밀번호 형식입니다";
  pwMessage.classList.add("confirm");
  pwMessage.classList.remove("error");
  checkObj.memberPw = true; // 유효한 비밀번호임을 명시

  // 비밀번호 입력 시 비밀번호 확인란의 값과 비교하는 코드 추가

  // 비밀번호 확인에 값이 작성되었을 때
  if (memberPwConfirm.value.length > 0) {
    checkPw();
  }
});

// 6) 비밀번호 확인 유효성 검사
memberPwConfirm.addEventListener("input", () => {
  if (checkObj.memberPw) {
    // memberPw가 유효한 경우
    checkPw(); // 비교하는 함수 수행
    return;
  }

  // memberPw가 유효하지 않은 경우는 memberPwConfirm도 유효하지 않아야함
  checkObj.memberPwConfirm = false;
});

document.querySelector(".submit-btn").addEventListener("click", () => {
  fetch("/member/changePw", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj), // obj라는 JS 객체를 JSON으로 변경
  });
});
