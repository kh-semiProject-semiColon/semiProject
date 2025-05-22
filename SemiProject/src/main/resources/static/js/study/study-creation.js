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
  formData.append("periodType", form.periodType.value);

  // 기간이 개월인 경우 개월 수도 추가
  if (form.periodType.value === "개월") {
    formData.append("periodMonth", form.periodMonth.value || 1);
  }

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
      if (data.success) {
        alert("스터디가 성공적으로 생성되었습니다!");
        window.location.href = "/main"; // 성공 시 메인페이지로 이동
      } else {
        alert("스터디 생성에 실패했습니다: " + data.message);
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
