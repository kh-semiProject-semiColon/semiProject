// 프로필 조회 js --------------------------------------------------
document.addEventListener("DOMContentLoaded", function () {
  // 닉네임 input창 요소
  const memberNickname = document.querySelector("[name=memberNickname]");
  // 닉네임 span 요소
  const nickNameMessage = document.querySelector("#nickNameMessage");
  // 전화번호 input창 요소
  const memberTel = document.querySelector("[name=memberTel]");
  // 전화번호 span 요소
  const TelMessage = document.querySelector("#TelMessage");

  // 프로필 사진 x버튼 (삭제)
  const removeImageBtn = document.querySelector(".remove-image");

  // 이미지 선택 버튼
  const imageSelectBtn = document.querySelector(".image-select-btn");

  // 주소 검색 버튼 기능
  const addressSearchBtn = document.querySelector(".address-search");

  // ------------------------------------------------- 닉네임 유효 검사
  memberNickname.addEventListener("input", (e) => {
    const inputNickname = e.target.value;

    // 입력 안 한 경우
    if (inputNickname.trim().length === 0) {
      nickNameMessage.innerText = "한글,영어,숫자로만 2~10글자";
      nickNameMessage.classList.remove("confirm", "error");
      memberNickname.value = "";
      return;
    }

    // 2) 정규식 검사
    const regExp = /^[가-힣\w\d]{2,10}$/;

    if (!regExp.test(inputNickname)) {
      //유효하지 않은 경우
      nickNameMessage.innerText = "유효하지 않은 닉네임 형식입니다.";
      nickNameMessage.classList.add("error");
      nickNameMessage.classList.remove("confirm");
      return;
    }

    nickNameMessage.innerText = "사용가능한 닉네임입니다.";
    nickNameMessage.classList.add("confirm");
    nickNameMessage.classList.remove("error");
  });

  // ------------------------------------------------- 핸드폰 유효성검사

  memberTel.addEventListener("input", (e) => {
    const inputTel = e.target.value;

    if (inputTel.trim().length === 0) {
      TelMessage.innerText = "전화번호를 입력해주세요.(- 제외)";
      TelMessage.classList.remove("error", "confirm");
      TelMessage.value = "";
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

  // 이미지 제거 기능
  removeImageBtn.addEventListener("click", function () {
    profileImage.src = "/images/default-profile.png";
    // 실제 구현 시에는 hidden input을 활용하여 이미지 제거 상태를 서버에 전송할 수 있음
  });

  // 이미지 선택 버튼 기능
  imageSelectBtn.addEventListener("click", function () {
    const fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.accept = "image/*";
    fileInput.click();

    fileInput.addEventListener("change", function () {
      if (this.files && this.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
          profileImage.src = e.target.result;
        };
        reader.readAsDataURL(this.files[0]);
      }
    });
  });

  // 다음 주소 API 다루기
  function execDaumPostcode() {
    new daum.Postcode({
      oncomplete: function (data) {
        // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

        // 각 주소의 노출 규칙에 따라 주소를 조합한다.
        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
        var addr = ""; // 주소 변수

        //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
        if (data.userSelectedType === "R") {
          // 사용자가 도로명 주소를 선택했을 경우
          addr = data.roadAddress;
        } else {
          // 사용자가 지번 주소를 선택했을 경우(J)
          addr = data.jibunAddress;
        }

        // 우편번호와 주소 정보를 해당 필드에 넣는다.
        document.getElementById("postcode").value = data.zonecode;
        document.getElementById("address").value = addr;
        // 커서를 상세주소 필드로 이동한다.
        document.getElementById("detailAddress").focus();
      },
    }).open();
  }

  addressSearchBtn.addEventListener("click", () => {
    execDaumPostcode();
  });

  // // 반응형 메뉴 토글
  // const menuButton = document.querySelector(".menu-button");
  // const navMenu = document.querySelector(".nav-menu");

  // menuButton.addEventListener("click", function () {
  //   navMenu.style.display = navMenu.style.display === "flex" ? "none" : "flex";
  // });
});
