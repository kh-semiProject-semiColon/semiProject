// th:data- 에서 저장한 값 지정된 자리에 집어넣기
document.addEventListener("DOMContentLoaded", function () {
  const studySelect = document.querySelector("select[name='studyNo']");

  studySelect.addEventListener("change", function () {
    const selected = this.options[this.selectedIndex];

    // 올바른 속성명 사용 (소문자)
    const studyMaxCount = selected.dataset.studymaxcount;
    const studyType = selected.dataset.studytype || "";
    const studyPeriod = selected.dataset.studyperiod || "";

    // 모집 인원
    document.querySelector("input[name='studyMaxCount']").value = studyMaxCount;

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
document.querySelectorAll("input[name='day']").forEach((checkbox) => {
  checkbox.addEventListener("click", () => {
    console.log(`${checkbox.value} 클릭됨`);
  });
});

// 폼 제출 시 처리
document.querySelector("form").addEventListener("submit", function (e) {
  e.preventDefault(); // 기본 제출 방지 (필요 시)

  // 체크된 요일들 수집
  const checkedDays = [];
  this.querySelectorAll("input[name='day']:checked").forEach((checkbox) => {
    checkedDays.push(checkbox.value);
  });

  // 쉼표로 연결된 문자열 생성
  const dayString = checkedDays.join(", ");

  // 기존 체크박스들 비활성화
  this.querySelectorAll("input[name='day']").forEach((checkbox) => {
    checkbox.disabled = true;
  });

  // 숨겨진 input 생성
  const hiddenInput = document.createElement("input");
  hiddenInput.type = "hidden";
  hiddenInput.name = "day";
  hiddenInput.value = dayString;

  // 폼에 추가
  this.appendChild(hiddenInput);

  // 실제 제출
  this.submit();
});
