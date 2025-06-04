function redirectToMain() {
  // 메인 페이지로 리다이렉트
  window.location.href = "/"; // 실제 메인 페이지 URL로 변경
}

function previewImage(event) {
  const file = event.target.files[0];
  const preview = document.getElementById("imagePreview");

  if (file) {
    const reader = new FileReader();
    reader.onload = function (e) {
      preview.innerHTML = `<img src="${e.target.result}" alt="미리보기" style="width: 100%; height: 100%; object-fit: cover;">`;
    };
    reader.readAsDataURL(file);
  } else {
    preview.innerHTML = "이미지를 선택해주세요";
  }
}

// 중복검사 후 바뀌는 스터디명을 알아채기 위한 플래그
let flag = false;

// 스터디명 중복검사
document.querySelector(".studyNameConfirm").addEventListener("click", () => {
  const studyName = document.getElementById("studyName");
  const studyNameConfirm = document.getElementById("studyNameConfirmSpan");
  if (studyName.value.trim().length == 0) {
    alert("스터디명을 입력해 주세요");
    return;
  }
  fetch("/study/confirm", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ studyName: studyName.value }),
  })
    .then((resp) => resp.text())
    .then((result) => {
      if (parseInt(result) > 0) {
        studyNameConfirm.textContent = "중복된 스터디명 입니다.";
        studyNameConfirm.style.color = "red";
        studyNameConfirm.style.fontSize = "14px";
        flag = false;
      } else {
        studyNameConfirm.textContent = "사용 가능한 스터디명 입니다.";
        studyNameConfirm.style.color = "green";
        studyNameConfirm.style.fontSize = "14px";
        flag = true;
      }
    });
});

// 중복검사 후 달라진 스터디명에 대한 재 중복검사 요청
document.querySelector("#studyName").addEventListener("input", () => {
  if (flag == true) {
    const studyNameConfirm = document.getElementById("studyNameConfirmSpan");
    console.log("바꾼다!바꾼다!");
    studyNameConfirm.textContent = "";
    flag = false;
  }
});

function submitForm(event) {
  event.preventDefault();
  if (flag === false) {
    alert("스터디명 중복검사를 해주세요");
    return;
  }

  // 폼 데이터 수집
  const formData = new FormData();
  const form = document.getElementById("studyForm");

  // 기본 데이터 추가
  formData.append("studyName", form.studyName.value);
  formData.append("studyMaxCount", form.studyMaxCount.value);
  formData.append("studyType", form.studyType.value);

  // 스터디 운영기간에서 "무기한"을 선택한 경우 studyPeriod의 값이 빈 문자열로 들어옴
  // Study DTO에서는 studyPeriod는 int형으로 정의 되어 있으므로 바꿔주는 과정이 필요
  if (form.studyPeriod.value === "") {
    form.studyPeriod.value = 0;
  }
  formData.append("studyPeriod", form.studyPeriod.value);

  // 이미지 파일 추가
  if (form.profileImage.files[0]) {
    formData.append("profileImage", form.profileImage.files[0]);
  }

  // 컨트롤러로 데이터 전송
  fetch("/study/create", {
    method: "POST",
    body: formData,
  })
    .then((response) => response.json())
    .then((data) => {
      if (data === 1) {
        alert("스터디가 성공적으로 생성되었습니다!");
        window.location.href = "/"; // 성공 시 메인페이지로 이동
      } else {
        if (data === -1) {
          alert("스터디 생성에 실패했습니다");
          window.location.href = "/"; // 실패 시 메인페이지로 이동 후 alert 따로 컨트롤러에서 요청
        } else {
          alert("스터디명은 10글자 이내로 작성해주세요");
          return;
        }
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("스터디 생성 중 오류가 발생했습니다.");
    });
}

// 무기한 선택 시 개월 입력 비활성화
document.querySelectorAll('input[name="periodType"]').forEach((radio) => {
  radio.addEventListener("change", function () {
    const monthInput = document.getElementById("periodMonth");
    if (this.value === "무기한") {
      monthInput.disabled = true;
      monthInput.value = "";
    } else {
      monthInput.disabled = false;
    }
  });
});
