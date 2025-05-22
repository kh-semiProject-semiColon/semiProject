// 게시글 목록 + 검색 + 정렬 + 페이징
document.addEventListener("DOMContentLoaded", () => {
  loadPage(1);

  const keywordInput = document.getElementById("searchKeyword");
  if (keywordInput) keywordInput.addEventListener("input", () => loadPage(1));

  const sortSelect = document.getElementById("sortOption");
  if (sortSelect) sortSelect.addEventListener("change", () => loadPage(1));
});

function loadPage(page) {
  const studyNo = document.getElementById("selectedStudyNo")?.value || 0;
  const key = document.getElementById("searchKey")?.value || "";
  const keyword = document.getElementById("searchKeyword")?.value || "";
  const sort = document.getElementById("sortOption")?.value || "new";

  fetch(`/studyBoard/list/${studyNo}?page=${page}&key=${key}&keyword=${keyword}&sort=${sort}`)
    .then(res => res.text())
    .then(html => {
      document.getElementById("board-list-container").innerHTML = html;
      attachPaginationEvents();
    });
}

function attachPaginationEvents() {
  document.querySelectorAll(".pagination-link").forEach(link => {
    link.addEventListener("click", e => {
      e.preventDefault();
      loadPage(e.target.dataset.page);
    });
  });
}
