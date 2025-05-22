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

function submitForm(event) {
  event.preventDefault();

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
        alert("스터디 생성에 실패했습니다");
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
