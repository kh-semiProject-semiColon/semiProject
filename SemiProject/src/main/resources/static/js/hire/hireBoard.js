document.addEventListener("DOMContentLoaded", () => {
  const writeBtn = document.querySelector(".writeBtn");

  if (writeBtn) {
    writeBtn.addEventListener("click", () => {
      location.href = "/hire/write";
    });
  }
});
