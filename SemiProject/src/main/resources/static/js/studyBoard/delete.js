// 일반 멤버 탈퇴 확인
document.addEventListener("DOMContentLoaded", function () {
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
});

// 스터디 해체 확인
function confirmStudyDeletion() {
  const studyName = "[[${study.studyName}]]";
  const memberCount = "[[${study.currentMemberCount}]]";

  const message =
    `정말로 "${studyName}" 스터디를 해체하시겠습니까?\n\n` +
    `현재 ${memberCount}명의 멤버가 있습니다.\n` +
    `해체 시 모든 멤버가 자동으로 탈퇴되며, 되돌릴 수 없습니다.\n\n` +
    `계속하시겠습니까?`;

  if (confirm(message)) {
    // 스터디 해체 요청
    const form = document.createElement("form");
    form.method = "POST";
    form.action = "/study/[[${study.studyNo}]]/dissolve";

    // CSRF 토큰 추가
    const csrfToken = document.createElement("input");
    csrfToken.type = "hidden";
    csrfToken.name = "[[${_csrf.parameterName}]]";
    csrfToken.value = "[[${_csrf.token}]]";
    form.appendChild(csrfToken);

    document.body.appendChild(form);
    form.submit();
  }
}
