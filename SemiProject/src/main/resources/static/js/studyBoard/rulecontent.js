// 글자 수 카운터 업데이트
function updateCharCount() {
  const textarea = document.getElementById("ruleContent");
  const charCount = document.getElementById("charCount");
  const count = textarea.value.length;

  charCount.textContent = count;

  if (count > 4000) {
    charCount.parentElement.classList.add("over-limit");
  } else {
    charCount.parentElement.classList.remove("over-limit");
  }
}

// 모달 열기
function openRuleModal() {
  const modal = document.getElementById("ruleModal");
  const overlay = document.getElementById("modalOverlay");

  modal.style.display = "block";
  overlay.style.display = "block";
  document.body.style.overflow = "hidden";

  // 애니메이션을 위한 클래스 추가
  setTimeout(() => {
    modal.classList.add("show");
    overlay.classList.add("show");
  }, 10);

  // 글자 수 카운터 초기화
  updateCharCount();
}

// 모달 닫기
function closeRuleModal() {
  const modal = document.getElementById("ruleModal");
  const overlay = document.getElementById("modalOverlay");

  modal.classList.remove("show");
  overlay.classList.remove("show");

  setTimeout(() => {
    modal.style.display = "none";
    overlay.style.display = "none";
    document.body.style.overflow = "auto";
  }, 300);
}

// ESC 키로 모달 닫기
document.addEventListener("keydown", function (e) {
  if (e.key === "Escape") {
    closeRuleModal();
  }
});

// 글자 수 카운터 이벤트 리스너
document
  .getElementById("ruleContent")
  .addEventListener("input", updateCharCount);

// 폼 비동기 처리
document.getElementById("ruleForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const submitBtn = this.querySelector('button[type="submit"]');
  const formData = new FormData(this);
  const studyNo = formData.get("studyNo");
  const ruleContent = formData.get("ruleContent");

  // 로딩 상태 표시
  submitBtn.classList.add("loading");
  submitBtn.disabled = true;

  // 실제 환경에서 CSRF 토큰이 필요한 경우:
  // const csrfToken = document.querySelector('[name="_csrf"]').value;

  // 시뮬레이션을 위한 setTimeout (실제로는 fetch 사용)
  setTimeout(() => {
    // 성공 시뮬레이션
    console.log("내규 업데이트:", { studyNo, ruleContent });

    // 페이지의 내규 내용 업데이트
    document.querySelector(".rule-text").innerHTML = ruleContent.replace(
      /\n/g,
      "<br>"
    );

    // 로딩 상태 해제
    submitBtn.classList.remove("loading");
    submitBtn.disabled = false;

    // 성공 메시지 표시 (실제로는 더 세련된 토스트 메시지 사용)
    alert("내규가 성공적으로 수정되었습니다.");

    // 모달 닫기
    closeRuleModal();
  }, 1500); // 1.5초 로딩 시뮬레이션

  //  실제 AJAX 코드 (주석 처리)
  fetch(`/study/${studyNo}/rule`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "X-Requested-With": "XMLHttpRequest",
      // CSRF 토큰이 필요한 경우: 'X-CSRF-TOKEN': csrfToken
    },
    body: JSON.stringify({
      studyNo: studyNo,
      ruleContent: ruleContent,
    }),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((data) => {
      if (data.success) {
        location.reload();
      } else {
        alert(data.message || "내규 저장에 실패했습니다.");
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("서버 오류가 발생했습니다.");
    })
    .finally(() => {
      submitBtn.classList.remove("loading");
      submitBtn.disabled = false;
    });
});

// 페이지 로드 시 글자 수 카운터 초기화
document.addEventListener("DOMContentLoaded", function () {
  updateCharCount();
});
