// 멤버 강퇴
function kickMember(studyNo, memberNo) {
  if (!confirm("정말 강퇴하시겠습니까?")) return;
  fetch(`/study/member/kick`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ studyNo, memberNo })
  })
  .then(res => res.text())
  .then(result => {
    if (result === "1") {
      alert("강퇴 완료");
      location.reload();
    }
  });
}
