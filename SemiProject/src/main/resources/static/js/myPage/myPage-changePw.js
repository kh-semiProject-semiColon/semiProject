const changePwForm = document.querySelector(".changePw");

const originPw = document.querySelector("#originPw");
const newPw = document.querySelector("#newPw");
const newPwConfirm = document.querySelector("#newPwConfirm");

const originPwMessage = document.querySelector("#originPwMessage");
const changePwMessage = document.querySelector("#changePwMessage");
const changeConfirmMessage = document.querySelector("#changeConfirmMessage");

// // 입력 시 메시지 초기화
// originPw.addEventListener("input", (e) => {
//   if (e.target.value.trim().length === 0) {
//     originPwMessage.innerText = "비밀번호를 입력해주세요";
//     originPwMessage.classList.add("error");
//   } else {
//     originPwMessage.innerText = "";
//     originPwMessage.classList.remove("error");
//   }
// });

newPw.addEventListener("input", (e) => {
  const value = e.target.value.trim();
  const regExp = /^[a-zA-Z0-9!@#_-]{2,100}$/;

  if (value.length === 0) {
    changePwMessage.innerText = "새 비밀번호를 입력해주세요";
    changePwMessage.classList.add("error");
  } else if (!regExp.test(value)) {
    changePwMessage.innerText = "비밀번호가 유효하지 않습니다.";
    changePwMessage.classList.add("error");
  } else {
    changePwMessage.innerText = "유효한 비밀번호입니다";
    changePwMessage.classList.remove("error");
    changePwMessage.classList.add("confirm");
  }
});

newPwConfirm.addEventListener("input", (e) => {
  if (newPw.value !== newPwConfirm.value) {
    changeConfirmMessage.innerText = "비밀번호가 일치하지 않습니다.";
    changeConfirmMessage.classList.add("error");
  } else {
    changeConfirmMessage.innerText = "비밀번호가 일치합니다.";
    changeConfirmMessage.classList.remove("error");
    changeConfirmMessage.classList.add("confirm");
  }
});

// 폼 제출 시 최종 유효성 검사
changePwForm.addEventListener("submit", (e) => {
  let hasError = false;

  if (originPw.value.trim() === "") {
    originPwMessage.innerText = "비밀번호를 입력해주세요";
    originPwMessage.classList.add("error");
    hasError = true;
  }

  const regExp = /^[a-zA-Z0-9!@#_-]{2,100}$/;

  if (!regExp.test(newPw.value)) {
    changePwMessage.innerText = "비밀번호가 유효하지 않습니다.";
    changePwMessage.classList.add("error");
    hasError = true;
  }

  if (newPw.value !== newPwConfirm.value) {
    changeConfirmMessage.innerText = "비밀번호가 일치하지 않습니다.";
    changeConfirmMessage.classList.add("error");
    hasError = true;
  }

  if (hasError) {
    e.preventDefault();
  }
});
