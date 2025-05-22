document.querySelectorAll(".sidebar-menu-item").forEach((item) => {
  item.addEventListener("click", function () {
    document.querySelectorAll(".sidebar-menu-item").forEach((i) => {
      i.classList.remove("active");
    });
    this.classList.add("active");
  });
});

const submitBtn = document.querySelector(".submit-btn");

submitBtn.addEventListener("click", () => {
  location.href = "/";
});
