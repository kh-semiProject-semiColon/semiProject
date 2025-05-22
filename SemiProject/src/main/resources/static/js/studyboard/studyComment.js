// 댓글 등록, 수정, 삭제 Ajax 처리
function insertComment(boardNo) {
  const content = document.getElementById("commentContent").value;
  fetch(`/studyComment/insert`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ boardNo, content })
  })
  .then(res => res.text())
  .then(result => {
    if (result === "1") {
      loadComments(boardNo);
      document.getElementById("commentContent").value = "";
    }
  });
}

function loadComments(boardNo) {
  fetch(`/studyComment/list/${boardNo}`)
    .then(res => res.text())
    .then(html => {
      document.getElementById("commentList").innerHTML = html;
    });
}
