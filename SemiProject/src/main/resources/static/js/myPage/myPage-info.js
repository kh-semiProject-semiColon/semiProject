let statusCheck = -1; // 이미지 변경 상태 체크 전역 변수

document.addEventListener("DOMContentLoaded", function () {
  const memberNickname = document.querySelector("[name=memberNickname]");
  const nickNameMessage = document.querySelector("#nickNameMessage");
  const memberTel = document.querySelector("[name=memberTel]");
  const TelMessage = document.querySelector("#TelMessage");
  const addressSearchBtn = document.querySelector(".address-search");
  const removeImageBtn = document.querySelector(".remove-image");
  const imageSelectBtn = document.querySelector(".image-select-btn");

  const postcode = document.getElementById("postcode");
  const address = document.getElementById("address");
  const detailAddress = document.getElementById("detailAddress");
  const memberIntroduce = document.querySelector("[name=memberIntroduce]");
  const memberMajor = document.querySelectorAll("[name=memberMajor]");

  const initialValues = {
    nickname: memberNickname.value,
    tel: memberTel.value,
    postcode: postcode.value,
    address: address.value,
    detailAddress: detailAddress.value,
    introduce: memberIntroduce.value,
    major: document.querySelector("[name=memberMajor]:checked")?.value || "",
  };

  function hasChanges() {
    if (statusCheck !== -1) return true;
    if (memberNickname.value !== initialValues.nickname) return true;
    if (memberTel.value !== initialValues.tel) return true;
    if (postcode.value !== initialValues.postcode) return true;
    if (address.value !== initialValues.address) return true;
    if (detailAddress.value !== initialValues.detailAddress) return true;
    if (memberIntroduce.value !== initialValues.introduce) return true;

    const currentMajor =
      document.querySelector("[name=memberMajor]:checked")?.value || "";
    if (currentMajor !== initialValues.major) return true;

    return false;
  }

  // 닉네임 유효성 검사
  memberNickname.addEventListener("input", (e) => {
    const inputNickname = e.target.value;

    if (inputNickname.trim().length === 0) {
      nickNameMessage.innerText = "한글,영어,숫자로만 2~10글자";
      nickNameMessage.classList.remove("confirm", "error");
      memberNickname.value = "";
      return;
    }

    const regExp = /^[가-힣\w\d]{2,10}$/;

    if (!regExp.test(inputNickname)) {
      nickNameMessage.innerText = "유효하지 않은 닉네임 형식입니다.";
      nickNameMessage.classList.add("error");
      nickNameMessage.classList.remove("confirm");
      return;
    }

    nickNameMessage.innerText = "사용가능한 닉네임입니다.";
    nickNameMessage.classList.add("confirm");
    nickNameMessage.classList.remove("error");
  });

  // 전화번호 유효성 검사
  memberTel.addEventListener("input", (e) => {
    const inputTel = e.target.value;

    if (inputTel.trim().length === 0) {
      TelMessage.innerText = "전화번호를 입력해주세요.(- 제외)";
      TelMessage.classList.remove("error", "confirm");
      return;
    }

    const regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;

    if (!regExp.test(inputTel)) {
      TelMessage.innerText = "유효하지 않은 전화번호 형식입니다.";
      TelMessage.classList.add("error");
      TelMessage.classList.remove("confirm");
      return;
    }

    TelMessage.innerText = "유효한 전화번호 형식입니다.";
    TelMessage.classList.remove("error");
    TelMessage.classList.add("confirm");
  });

  // 다음 주소 API
  function execDaumPostcode() {
    new daum.Postcode({
      oncomplete: function (data) {
        let addr =
          data.userSelectedType === "R" ? data.roadAddress : data.jibunAddress;
        document.getElementById("postcode").value = data.zonecode;
        document.getElementById("address").value = addr;
        document.getElementById("detailAddress").focus();
      },
    }).open();
  }

  addressSearchBtn.addEventListener("click", () => {
    execDaumPostcode();
  });

  // 이미지 업로드 관련
  const profileForm = document.getElementById("profile");

  if (profileForm != null) {
    const profileImg = document.getElementById("profileImg");
    const imageInput = document.getElementById("imageInput");
    const MAX_SIZE = 1024 * 1024 * 5;
    const defaultImageUrl = `${window.location.origin}/images/default-profile.png`;

    let previousImage = profileImg.src;
    let previousFile = null;

    imageSelectBtn.addEventListener("click", () => {
      imageInput.click();
    });

    imageInput.addEventListener("change", () => {
      const file = imageInput.files[0];

      if (file) {
        if (file.size <= MAX_SIZE) {
          const newImageUrl = URL.createObjectURL(file);
          profileImg.src = newImageUrl;
          previousImage = newImageUrl;
          previousFile = file;
          statusCheck = 1;
        } else {
          alert("5MB 이하의 이미지를 선택해주세요.");
          imageInput.value = "";
          profileImg.src = previousImage;

          if (previousFile != null) {
            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(previousFile);
            imageInput.files = dataTransfer.files;
          }
        }
      } else {
        profileImg.src = previousImage;

        if (previousFile) {
          const dataTransfer = new DataTransfer();
          dataTransfer.items.add(previousFile);
          imageInput.files = dataTransfer.files;
        }
      }
    });

    removeImageBtn.addEventListener("click", () => {
      if (profileImg.src !== defaultImageUrl) {
        imageInput.value = "";
        profileImg.src = defaultImageUrl;
        statusCheck = 0;
        previousImage = defaultImageUrl;
        previousFile = null;
      } else {
        statusCheck = -1;
      }
    });

    // 폼 제출 시 유효성 및 변경사항 검사
    profileForm.addEventListener("submit", (e) => {
      console.log("변경됨?", hasChanges()); // ← 디버깅용 (원하면 삭제)

      if (!hasChanges()) {
        e.preventDefault();
        alert("변경사항이 없습니다.");
        return;
      }

      if (nickNameMessage.classList.contains("error")) {
        e.preventDefault();
        alert("닉네임을 올바르게 입력해주세요.");
        return;
      }

      if (TelMessage.classList.contains("error")) {
        e.preventDefault();
        alert("전화번호를 올바르게 입력해주세요.");
        return;
      }
    });
  }
});
