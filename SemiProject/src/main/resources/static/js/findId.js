document.querySelectorAll(".sidebar-menu-item").forEach((item) => {
  item.addEventListener("click", function () {
    document.querySelectorAll(".sidebar-menu-item").forEach((i) => {
      i.classList.remove("active");
    });
    this.classList.add("active");
  });
});

const form = document.querySelector("form");
const submitBtn = document.querySelector(".submit-btn");
const memberName = document.querySelector("#memberName");
const memberTel = document.querySelector("#memberTel");

form.addEventListener("submit", (e) => {
  e.preventDefault();

  const inputname = memberName.value;
  const inputtel = memberTel.value;

  if (inputname.trim().length === 0 || inputtel.trim().length === 0) {
    alert("찾으려는 회원 정보를 입력하세요");
    return;
  }

  const obj = {
    memberName: inputname,
    memberTel: inputtel,
  };

  console.log(obj);
  // 이름, 전화번호 중복 검사
  fetch("/member/checkName", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj),
  })
    .then((resp) => resp.text())
    .then((count) => {
      console.log(count);
      if (count !== "1") {
        alert("일치하는 회원 정보가 없습니다.");
        return;
      }
      form.submit();
    })
    .catch((err) => console.log(err));
});
