const title = document.querySelector(".title");

const content = document.querySelector("#summernote");

document.querySelector(".board-write").addEventListener("submit", (e) => {
  e.preventDefault();

  if (title.value.trim().length === 0 || content.value.trim().length === 0) {
    alert("제목과 내용을 입력해주세요");
    return;
  }

  e.target.submit();
});
