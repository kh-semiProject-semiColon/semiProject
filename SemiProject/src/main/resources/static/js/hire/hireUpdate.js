document.addEventListener("DOMContentLoaded", function () {
  const studyInput = document.querySelector("#studyDisplay");

  const studyType = studyInput.dataset.studytype || "";
  const studyPeriod = studyInput.dataset.studyperiod || "";

  // 방향성 (라디오 버튼)
  const directionRadio = document.querySelector(
    `input[name='direction'][value='${studyType}']`
  );
  if (directionRadio) directionRadio.checked = true;

  // 기간
  if (studyPeriod == 0) {
    document.querySelector("#period-unlimited").checked = true;
  } else {
    document.querySelector("#period-specific").checked = true;
    document.querySelector("#periodNum").value = studyPeriod;
  }
});
// 기본 선택 시 초기화하고 싶다면 아래 한 줄 추가
// studySelect.dispatchEvent(new Event("change"));

// 체크박스 타입 체크박스 라디오 모양으로 바꾸고 체크 언체크하기
const dayCheck = document.querySelectorAll("input[name='day']");

// dayCheck.addEventListener("click", () => {
//   dayCheck.checked = !dayCheck.checked;
// });

// 요일들 하나의 문자열로 합치기
// 체크박스 클릭 시 콘솔 출력
document.querySelectorAll("input[name='dayCanJoin1']").forEach((checkbox) => {
  checkbox.addEventListener("click", () => {
    console.log(`${checkbox.value} 클릭됨`);
  });
});

// 폼 제출 시 처리
document
  .getElementById("hireWriteFrm")
  .addEventListener("submit", function (e) {
    e.preventDefault(); // 기본 제출 방지

    const form = this;

    // 체크된 요일 수집
    const checkedDays = Array.from(
      form.querySelectorAll("input[name='dayCanJoin1']:checked")
    ).map((cb) => cb.value);

    // 예외 처리: 하나도 선택 안 했을 경우 (필요 시)
    if (checkedDays.length === 0) {
      alert("스터디 요일을 하나 이상 선택해주세요.");
      return;
    }

    const dayCanJoin = checkedDays.join(" / ");

    // 기존 체크박스 비활성화 (서버 전송 안 되게)
    form
      .querySelectorAll("input[name='dayCanJoin1']")
      .forEach((cb) => (cb.disabled = true));

    // 기존 dayCanJoin hidden input이 있으면 제거
    const existingHidden = form.querySelector("input[name='dayCanJoin']");
    if (existingHidden) existingHidden.remove();

    // 새 hidden input 추가
    const hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.name = "dayCanJoin";
    hiddenInput.value = dayCanJoin;
    form.appendChild(hiddenInput);

    console.log("dayCanJoin:", dayCanJoin);
    console.log("form 내용:", new FormData(form));

    const formData = new FormData(form);
    for (let [key, value] of formData.entries()) {
      console.log(`${key}: ${value}`);
    }

    form.submit(); // 실제 제출
  });
