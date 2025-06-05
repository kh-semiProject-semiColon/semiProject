// 글쓰기 버튼 클릭 시
const insertBtn = document.querySelector(".write-button");

// 글쓰기 버튼이 존재할 때 (로그인 상태일 때만)
if (insertBtn != null) {
  insertBtn.addEventListener("click", () => {
    // get 방식으로 글작성 가능한 페이지로 이동 요청 전송
    // /editBoard/게시판번호(1)/insert

    location.href = `/editBoard/${boardCode}/insert`;
  });
}

const input = document.querySelector(".search-input");

document.querySelector("#boardSearch").addEventListener("submit", (e) => {
  e.preventDefault();

  if (input.value.length >= 30) {
    alert("검색어를 30글자 이내로 작성해주세요.");
    return;
  }

  e.target.submit();
});
