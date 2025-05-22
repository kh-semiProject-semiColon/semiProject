// 글쓰기 유효성 검사 및 전송
function validateAndSubmit() {
  const title = document.getElementById("boardTitle").value.trim();
  const content = document.getElementById("boardContent").value.trim();
  if (title === "" || content === "") {
    alert("제목과 내용을 입력해주세요.");
    return false;
  }
  return true;
}
