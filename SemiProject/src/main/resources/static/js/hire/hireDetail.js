document.querySelector(".post-btn").addEventListener("click", (event) => {
  const hireNo = event.target.dataset.hireno;
  location.href = "/hire/edit" + hireNo;
});
