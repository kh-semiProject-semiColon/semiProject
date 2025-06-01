// th:data- 에서 저장한 값 지정된 자리에 집어넣기
document.addEventListener("DOMContentLoaded", function () {
  const studySelect = document.querySelector("select[name='studyNo']");

  const selected = studySelect.options[studySelect.selectedIndex];

  // 올바른 속성명 사용 (소문자)
  const studyMaxCount = Number(selected.dataset.studymaxcount);
  const currentMemberCount = Number(selected.dataset.currentmembercount);
  
  const studyType = selected.dataset.studytype || "";
  const studyPeriod = selected.dataset.studyperiod || "";

  const hireCount = Math.max(0, studyMaxCount - currentMemberCount);

  // 모집 인원
  // hireCountSelect = document.querySelector("select[name='hireCount']");

  // hireCountSelect.innerHTML = '';

  // hireCountSelect.innerHTML = '<option value="" disabled selected>선택</option>';
  
  // for(let i=1; i<=hireCount; i++) {
  //   const option = document.createElement("option");
  //   option.value = i;
  //   option.textContent = i + "명";
  //   hireCountSelect.appendChild(option);
  // }

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
document.querySelectorAll("input[name='dayCanJoin']").forEach((checkbox) => {
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
      form.querySelectorAll("input[name='dayCanJoin']:checked")
    ).map((cb) => cb.value);

    // 예외 처리: 하나도 선택 안 했을 경우 (필요 시)
    if (checkedDays.length === 0) {
      alert("스터디 요일을 하나 이상 선택해주세요.");
      return;
    }

    const dayCanJoin = checkedDays.join(" / ");

    // 기존 체크박스 비활성화 (서버 전송 안 되게)
    form
      .querySelectorAll("input[name='dayCanJoin']")
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

    form.submit(); // 실제 제출
  });
