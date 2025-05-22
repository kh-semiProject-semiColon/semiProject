let agreeCheckBox = document.querySelectorAll(".agreement input");
document.querySelector("#agreeAll").addEventListener("change", (e) => {
  console.log(agreeCheckBox[0]);
  agreeCheckBox[0].checked = true;
  agreeCheckBox[1].checked = true;
  agreeCheckBox[2].checked = true;
  agreeCheckBox[3].checked = true;

  if (!e.target.checked) {
    agreeCheckBox[0].checked = false;
    agreeCheckBox[1].checked = false;
    agreeCheckBox[2].checked = false;
    agreeCheckBox[3].checked = false;
  }
});

document.querySelector(".btn-next").addEventListener("click", () => {
  if (
    !agreeCheckBox[0].checked ||
    !agreeCheckBox[1].checked ||
    !agreeCheckBox[2].checked ||
    !agreeCheckBox[3].checked
  ) {
    alert("모든 동의란을 체크해주세요.");
  } else {
    location.href = "/member/signup2";
  }
});
