// -------------------------------- delete3 페이지 js

// DOM 요소들
const reasonRadios = document.querySelectorAll(
  'input[name="withdrawalReason"]'
);
const otherReasonSection = document.getElementById("otherReasonSection");
const otherReasonText = document.getElementById("otherReasonText");
const withdrawalBtn = document.getElementById("withdrawalBtn");

// 라디오 버튼 변경 이벤트
reasonRadios.forEach((radio) => {
  radio.addEventListener("change", function () {
    // 기타 사항 선택 시 텍스트 영역 활성화
    if (this.value === "기타 사항") {
      otherReasonSection.classList.add("active");
      otherReasonText.disabled = false;
      otherReasonText.focus();
    } else {
      otherReasonSection.classList.remove("active");
      otherReasonText.disabled = true;
      otherReasonText.value = "";
    }

    // 버튼 활성화 체크
    checkSubmitButton();
  });
});

// 기타 사유 텍스트 입력 이벤트
otherReasonText.addEventListener("input", checkSubmitButton);

// 제출 버튼 활성화 체크 함수
function checkSubmitButton() {
  const selectedReason = document.querySelector(
    'input[name="withdrawalReason"]:checked'
  );
  let canSubmit = false;

  if (selectedReason) {
    if (selectedReason.value === "기타 사항") {
      // 기타 사항 선택 시 텍스트가 입력되어야 함
      canSubmit = otherReasonText.value.trim().length > 0;
    } else {
      // 다른 사유 선택 시 바로 활성화
      canSubmit = true;
    }
  }

  withdrawalBtn.disabled = !canSubmit;
}

// 탈퇴 버튼 클릭 이벤트
withdrawalBtn.addEventListener("click", function (e) {
  const selectedReason = document.querySelector(
    'input[name="withdrawalReason"]:checked'
  );
  let reasonText = selectedReason?.value;

  if (!reasonText) {
    alert("탈퇴 사유를 선택해주세요.");
    e.preventDefault();
    return;
  }

  if (reasonText === "기타 사항") {
    reasonText = otherReasonText.value.trim();
  }

  const confirmed = confirm(
    `탈퇴 사유: ${reasonText}\n\n정말로 회원탈퇴를 진행하시겠습니까? 이 작업은 되돌릴 수 없습니다.`
  );

  if (!confirmed) {
    e.preventDefault(); // 탈퇴 중지
  }

  // 확인된 경우 form 그대로 제출됨
});
