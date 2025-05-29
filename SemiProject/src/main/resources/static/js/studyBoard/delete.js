// DOM이 로드된 후에 실행
document.addEventListener("DOMContentLoaded", function () {
  console.log("DOM 로드 완료, STUDY_DATA:", STUDY_DATA);

  // ===== 함수 정의 =====

  // 모달 열기 (CSS 충돌 완전 해결)
  function openTransferModal() {
    console.log("팀장 위임 모달 열기");
    const modal = document.getElementById("transferModal");
    if (modal) {
      // CSS 충돌을 완전히 무시하고 강제 적용
      modal.style.cssText = `
        display: block !important;
        position: fixed !important;
        top: 0 !important;
        left: 0 !important;
        width: 100% !important;
        height: 100% !important;
        background-color: rgba(0, 0, 0, 0.5) !important;
        z-index: 99999 !important;
        transform: none !important;
        opacity: 1 !important;
      `;

      // 모달 콘텐츠도 강제 스타일 적용
      const modalContent = modal.querySelector(".modal-content");
      if (modalContent) {
        modalContent.style.cssText = `
          background-color: white !important;
          margin: 10% auto !important;
          padding: 20px !important;
          border: 1px solid #888 !important;
          width: 80% !important;
          max-width: 500px !important;
          border-radius: 8px !important;
          position: relative !important;
          transform: none !important;
          opacity: 1 !important;
          display: block !important;
        `;
      }

      console.log("모달 강제 스타일 적용 완료");
    } else {
      console.error("transferModal 요소를 찾을 수 없습니다");
      alert("모달을 찾을 수 없습니다. 페이지를 새로고침해주세요.");
    }
  }

  // 모달 닫기
  function closeTransferModal() {
    console.log("모달 닫기");
    const modal = document.getElementById("transferModal");
    if (modal) {
      modal.style.display = "none";
      const selectElement = document.getElementById("newLeaderSelect");
      if (selectElement) {
        selectElement.value = "";
      }
    }
  }

  // 위임 실행
  function executeTransfer() {
    console.log("위임 실행 함수 호출");
    const selectedLeaderId = document.getElementById("newLeaderSelect").value;

    if (!selectedLeaderId) {
      alert("새로운 팀장을 선택해주세요.");
      return;
    }

    const selectElement = document.getElementById("newLeaderSelect");
    const selectedLeaderName =
      selectElement.options[selectElement.selectedIndex].text;

    if (
      confirm(
        `${selectedLeaderName}에게 팀장 권한을 위임하고 탈퇴하시겠습니까?`
      )
    ) {
      console.log("위임 확인됨, 서버로 전송");

      // 폼 생성해서 서버에 전송
      const form = document.createElement("form");
      form.method = "POST";
      form.action = `/studyBoard/${STUDY_DATA.studyNo}/transfer-leadership`;

      const leaderInput = document.createElement("input");
      leaderInput.type = "hidden";
      leaderInput.name = "newLeaderId";
      leaderInput.value = selectedLeaderId;
      form.appendChild(leaderInput);

      document.body.appendChild(form);
      form.submit();
    }
  }

  // 스터디 해체 확인
  function confirmStudyDeletion() {
    console.log("스터디 해체 함수 호출");
    const studyName = STUDY_DATA.studyName;
    const memberCount = STUDY_DATA.memberCount;

    const message =
      `정말로 ${studyName} 스터디를 해체하시겠습니까?\n\n` +
      `현재 ${memberCount}명의 멤버가 있습니다.\n` +
      `해체 시 모든 멤버가 자동으로 탈퇴되며, 되돌릴 수 없습니다.\n\n` +
      `계속하시겠습니까?`;

    if (confirm(message)) {
      console.log("스터디 해체 확인됨");

      const form = document.createElement("form");
      form.method = "POST";
      form.action = `/study/${STUDY_DATA.studyNo}/dissolve`;

      document.body.appendChild(form);
      form.submit();
    }
  }

  // ===== 이벤트 리스너 등록 =====

  // 팀장 위임 버튼
  const transferBtn = document.getElementById("transferBtn");
  if (transferBtn) {
    transferBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      openTransferModal();
    });
    console.log("팀장 위임 버튼 이벤트 리스너 등록됨");
  } else {
    console.error("transferBtn을 찾을 수 없습니다");
  }

  // 스터디 해체 버튼
  const deleteStudyBtn = document.getElementById("deleteStudyBtn");
  if (deleteStudyBtn) {
    deleteStudyBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      confirmStudyDeletion();
    });
    console.log("스터디 해체 버튼 이벤트 리스너 등록됨");
  }

  // 모달 닫기 버튼들
  const modalCloseBtn = document.getElementById("modalCloseBtn");
  const modalCancelBtn = document.getElementById("modalCancelBtn");

  if (modalCloseBtn) {
    modalCloseBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      closeTransferModal();
    });
  }

  if (modalCancelBtn) {
    modalCancelBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      closeTransferModal();
    });
  }

  // 모달 확인 버튼
  const modalConfirmBtn = document.getElementById("modalConfirmBtn");
  if (modalConfirmBtn) {
    modalConfirmBtn.addEventListener("click", function (e) {
      e.preventDefault();
      e.stopPropagation();
      executeTransfer();
    });
  }

  // 일반 멤버 탈퇴 폼
  const withdrawalForm = document.querySelector(".withdrawal-form");
  if (withdrawalForm) {
    withdrawalForm.addEventListener("submit", function (e) {
      const isConfirmed = confirm(
        "정말로 스터디를 탈퇴하시겠습니까?\n탈퇴 후에는 해당 스터디의 모든 권한이 삭제됩니다."
      );
      if (!isConfirmed) {
        e.preventDefault();
      }
    });
  }

  // 모달 외부 클릭 시 닫기
  window.addEventListener("click", function (event) {
    const modal = document.getElementById("transferModal");
    if (event.target === modal) {
      closeTransferModal();
    }
  });

  console.log("모든 이벤트 리스너 등록 완료");

  // 디버깅: 모달 요소 확인
  const modal = document.getElementById("transferModal");
  console.log("모달 요소 확인:", modal);
  if (modal) {
    console.log("모달 현재 스타일:", window.getComputedStyle(modal));
  }
});
