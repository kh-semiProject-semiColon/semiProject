// 좋아요 등록/취소
function toggleLike(boardNo) {
  fetch(`/studyLike/toggle/${boardNo}`, { method: "POST" })
    .then(res => res.text())
    .then(result => {
      document.getElementById("likeCount").innerText = result;
    });
}
