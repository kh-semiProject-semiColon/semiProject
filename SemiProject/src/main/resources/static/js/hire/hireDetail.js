// document.querySelector(".post-btn").addEventListener("click", (event) => {
//   const hireNo = event.target.dataset.hireno;
//   location.href = "/hire/edit" + hireNo;
// });

// ------------------------------------------------------------------------------------------

function openResumeModal(memberNo) {
  if (!memberNo) {
    console.warn("❌ memberNo가 없습니다.");
    return;
  }

  fetch(`/hire/popup?memberNo=${memberNo}`)
    .then(res => res.json())
    .then(data => {
      document.getElementById('resumeNickname').textContent = data.memberNickname || "닉네임 없음";
      document.getElementById('resumeIntro').textContent = data.memberIntroduce || "자기소개 없음";
      document.getElementById('resumeProfileImg').src = data.profileImg || '/images/default_profile.png';
      document.getElementById('resumeMajor').textContent = data.memberMajor === 'Y' ? '○' : '×';

      // 모달 열기
      document.getElementById('resumeModal').style.display = 'flex';
    })
    .catch(err => {
      console.error("이력서 팝업 로딩 실패:", err);
      alert("이력서를 불러오는 중 오류가 발생했어요.");
    });
}

// 모달 닫기 처리
document.addEventListener("DOMContentLoaded", () => {
  const closeBtn = document.querySelector('.resume-close');
  if (closeBtn) {
    closeBtn.addEventListener('click', () => {
      document.getElementById('resumeModal').style.display = 'none';
    });
  }

  // 모달 백드롭 클릭 시 닫기
  const modal = document.getElementById('resumeModal');
  if (modal) {
    modal.addEventListener('click', (e) => {
      if (e.target === modal) {
        modal.style.display = 'none';
      }
    });
  }
});