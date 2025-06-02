let selectedImageFile = null;
let originalImageSrc = null;

// 페이지 로드 시 초기화
document.addEventListener("DOMContentLoaded", function () {
  const currentImage = document.getElementById("currentImage");
  if (currentImage) {
    originalImageSrc = currentImage.src;
  }

  // 파일 선택 이벤트
  document
    .getElementById("imageFile")
    .addEventListener("change", handleImageSelect);
});

// 이미지 선택 처리
function handleImageSelect(event) {
  const file = event.target.files[0];
  if (!file) return;

  // 파일 유효성 검사
  if (!validateImage(file)) {
    event.target.value = "";
    return;
  }

  selectedImageFile = file;
  previewImage(file);
}

// 이미지 유효성 검사
function validateImage(file) {
  // 파일 타입 체크
  const allowedTypes = ["image/jpeg", "image/jpg", "image/png"];
  if (!allowedTypes.includes(file.type)) {
    showAlert("오류", "JPG, PNG 파일만 업로드 가능합니다.");
    return false;
  }

  // 파일 크기 체크 (5MB)
  const maxSize = 5 * 1024 * 1024;
  if (file.size > maxSize) {
    showAlert("오류", "파일 크기는 5MB 이하여야 합니다.");
    return false;
  }

  return true;
}

// 이미지 미리보기
function previewImage(file) {
  const reader = new FileReader();
  reader.onload = function (e) {
    const imagePreview = document.getElementById("imagePreview");
    imagePreview.innerHTML = `
              <img src="${e.target.result}" alt="미리보기 이미지" class="default-img" id="currentImage" />
          `;
  };
  reader.readAsDataURL(file);
}

const imagePreview = document.getElementById("imagePreview");
const imgtag = imagePreview.cloneNode(true);

// 이미지 삭제
function removeImage() {
  selectedImageFile = null;
  document.getElementById("imageFile").value = "";
  const previewImgContainer = document.querySelector(".previewImgContainer");
  const newPreview = imgtag.cloneNode(true); // 새로운 복제본

  previewImgContainer.innerHTML = "";
  previewImgContainer.appendChild(newPreview);
}

// 폼 제출 처리
document
  .getElementById("studyUpdateForm")
  .addEventListener("submit", function (e) {
    e.preventDefault();

    const submitBtn = this.querySelector('button[type="submit"]');
    const formData = new FormData(this);

    // 선택된 이미지 파일이 있으면 추가
    if (selectedImageFile) {
      formData.set("imageFile", selectedImageFile);
    }

    // 로딩 상태 표시
    submitBtn.classList.add("loading");
    submitBtn.disabled = true;
    submitBtn.textContent = "수정 중...";

    //  실제 서버 통신 코드 (주석 처리)
    fetch("/studyBoard/update", {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("서버 응답 오류");
        }
        return response.json();
      })
      .then((data) => {
        if (data.success) {
          alert("수정 성공");
          location.reload();
        } else {
          showAlert("오류", data.message || "수정에 실패했습니다.");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        showAlert("오류", "서버 오류가 발생했습니다.");
      })
      .finally(() => {
        submitBtn.classList.remove("loading");
        submitBtn.disabled = false;
        submitBtn.textContent = "수정";
      });
  });

// 알림 모달 표시
function showAlert(title, message, callback) {
  document.getElementById("alertTitle").textContent = title;
  document.getElementById("alertMessage").textContent = message;
  document.getElementById("alertModal").style.display = "block";
  document.getElementById("modalOverlay").style.display = "block";
  document.body.style.overflow = "hidden";

  // 콜백 함수 저장
  window.alertCallback = callback;
}

// 알림 모달 닫기
function closeAlert() {
  document.getElementById("alertModal").style.display = "none";
  document.getElementById("modalOverlay").style.display = "none";
  document.body.style.overflow = "auto";

  // 콜백 함수 실행
  if (window.alertCallback) {
    window.alertCallback();
    window.alertCallback = null;
  }
}

// ESC 키로 모달 닫기
document.addEventListener("keydown", function (e) {
  if (e.key === "Escape") {
    closeAlert();
  }
});
