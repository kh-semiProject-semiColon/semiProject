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
document.addEventListener("DOMContentLoaded", function () {
  const ruleContentTextarea = document.getElementById("ruleContent");
  if (ruleContentTextarea) {
    ruleContentTextarea.addEventListener("input", updateCharCount);
    updateCharCount(); // 초기 글자 수 설정
  }
});

// 폼 비동기 처리 - 수정된 버전
document.addEventListener("DOMContentLoaded", function () {
  const ruleForm = document.getElementById("ruleForm");
  if (ruleForm) {
    ruleForm.addEventListener("submit", function (e) {
      e.preventDefault();

      const submitBtn = this.querySelector('button[type="submit"]');
      const formData = new FormData(this);
      const studyNo = formData.get("studyNo");
      const ruleContent = formData.get("ruleContent");

      // 유효성 검사
      if (!ruleContent || ruleContent.trim() === "") {
        alert("내규 내용을 입력해주세요.");
        return;
      }

      if (ruleContent.length > 4000) {
        alert("내규 내용은 4000자를 초과할 수 없습니다.");
        return;
      }

      // 로딩 상태 표시
      submitBtn.classList.add("loading");
      submitBtn.disabled = true;

      // CSRF 토큰 가져오기 (필요한 경우)
      const csrfToken = document.querySelector('meta[name="_csrf"]')
        ? document.querySelector('meta[name="_csrf"]').getAttribute("content")
        : null;
      const csrfHeader = document.querySelector('meta[name="_csrf_header"]')
        ? document
            .querySelector('meta[name="_csrf_header"]')
            .getAttribute("content")
        : null;

      // 헤더 설정
      const headers = {
        "Content-Type": "application/json",
        "X-Requested-With": "XMLHttpRequest",
      };

      // CSRF 토큰이 있으면 헤더에 추가
      if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
      }

      // 실제 AJAX 요청
      fetch("/studyBoard/rulecontent", {
        method: "POST",
        headers: headers,
        body: JSON.stringify({
          studyNo: parseInt(studyNo),
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
            alert("내규가 성공적으로 저장되었습니다.");
            // 페이지 새로고침으로 변경사항 반영
            location.reload();
          } else {
            alert(data.message || "내규 저장에 실패했습니다.");
          }
        })
        .catch((error) => {
          console.error("Error:", error);
          alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
        })
        .finally(() => {
          // 로딩 상태 해제
          submitBtn.classList.remove("loading");
          submitBtn.disabled = false;
        });
    });
  }
});
