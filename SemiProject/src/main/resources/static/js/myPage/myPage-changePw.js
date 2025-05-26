const changePwForm = document.querySelector(".changePw");

const originPw = document.querySelector("#originPw");
const newPw = document.querySelector("#newPw");
const newPwConfirm = document.querySelector("#newPwConfirm");

const originPwMessage = document.querySelector("#originPwMessage");
const changePwMessage = document.querySelector("#changePwMessage");
const changeConfirmMessage = document.querySelector("#changeConfirmMessage");

const checkObj = {
  originPw: false,
  newPw: false,
  newPwConfirm: false,
};

const regExp = /^[a-zA-Z0-9!@#_-]{2,100}$/;

// 비밀번호 일치 여부 확인 함수 (양방향에서 공통 사용)
function checkPasswordMatch() {
  if (newPwConfirm.value.length === 0 && newPw.value.length === 0) {
    changeConfirmMessage.innerText = "";
    changeConfirmMessage.classList.remove("error", "confirm");
    checkObj.newPwConfirm = false;
    return;
  }

  if (newPw.value !== newPwConfirm.value) {
    changeConfirmMessage.innerText = "비밀번호가 일치하지 않습니다.";
    changeConfirmMessage.classList.add("error");
    changeConfirmMessage.classList.remove("confirm");
    checkObj.newPwConfirm = false;
  } else {
    changeConfirmMessage.innerText = "비밀번호가 일치합니다.";
    changeConfirmMessage.classList.remove("error");
    changeConfirmMessage.classList.add("confirm");
    checkObj.newPwConfirm = true;
  }
}

// 새 비밀번호 유효성 검사 + 비밀번호 확인과의 일치 여부 검사
newPw.addEventListener("input", (e) => {
  const value = e.target.value.trim();

  if (value.length === 0) {
    changePwMessage.innerText = "새 비밀번호를 입력해주세요";
    changePwMessage.classList.add("error");
    changePwMessage.classList.remove("confirm");
    checkObj.newPw = false;
  } else if (!regExp.test(value)) {
    changePwMessage.innerText = "비밀번호가 유효하지 않습니다.";
    changePwMessage.classList.add("error");
    changePwMessage.classList.remove("confirm");
    checkObj.newPw = false;
  } else {
    changePwMessage.innerText = "유효한 비밀번호입니다";
    changePwMessage.classList.remove("error");
    changePwMessage.classList.add("confirm");
    checkObj.newPw = true;
  }

  // 일치 여부도 체크
  checkPasswordMatch();
});

// 비밀번호 확인 입력 시 일치 여부 검사
newPwConfirm.addEventListener("input", () => {
  checkPasswordMatch();
});

// 폼 제출 시 최종 유효성 검사
changePwForm.addEventListener("submit", (e) => {
  let hasError = false;

  if (originPw.value.trim() === "") {
    originPwMessage.innerText = "비밀번호를 입력해주세요";
    originPwMessage.classList.add("error");
    originPwMessage.classList.remove("confirm");
    hasError = true;
  }

  if (!regExp.test(newPw.value)) {
    changePwMessage.innerText = "비밀번호가 유효하지 않습니다.";
    changePwMessage.classList.add("error");
    changePwMessage.classList.remove("confirm");
    hasError = true;
  }

  if (newPw.value !== newPwConfirm.value) {
    changeConfirmMessage.innerText = "비밀번호가 일치하지 않습니다.";
    changeConfirmMessage.classList.add("error");
    changeConfirmMessage.classList.remove("confirm");
    hasError = true;
  }

  if (hasError) {
    e.preventDefault();
  }
});
