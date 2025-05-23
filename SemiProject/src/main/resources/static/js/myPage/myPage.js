// 공통 작동 js -------------------------------------
// 사이드바 메뉴 활성화
// location.pathname는 /myPage/info를 다운받습니다
document.addEventListener("DOMContentLoaded", function () {
  const currentLocation = location.pathname; // 현재 페이지 url

  const info = document.querySelector("#myPage-info");
  const posts = document.querySelector("#myPage-posts");
  const change = document.querySelector("#myPage-change");
  const delete1 = document.querySelector("#myPage-delete1");

  switch (currentLocation) {
    case "/myPage/info":
      info.classList.add("active");
      posts.classList.remove("active");
      change.classList.remove("active");
      delete1.classList.remove("active");
      break;

    case "/myPage/posts":
      info.classList.remove("active");
      posts.classList.add("active");
      change.classList.remove("active");
      delete1.classList.remove("active");
      break;

    case "/myPage/change":
      info.classList.remove("active");
      posts.classList.remove("active");
      change.classList.add("active");
      delete1.classList.remove("active");
      break;

    case "/myPage/withdraw":
      info.classList.remove("active");
      posts.classList.remove("active");
      change.classList.remove("active");
      delete1.classList.add("active");
      break;
  }
});

// 프로필 조회 js --------------------------------------------------
document.addEventListener("DOMContentLoaded", function () {
  const removeImageBtn = document.querySelector(".remove-image");
  const profileImage = document.querySelector(".profile-image");
  const imageSelectBtn = document.querySelector(".image-select-btn");
  const submitBtn = document.querySelector(".submit-btn");

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

  // 검색 버튼 기능
  const addressSearchBtn = document.querySelector(".address-search");

  addressSearchBtn.addEventListener("click", () => {
    execDaumPostcode();
  });

  // 반응형 메뉴 토글
  const menuButton = document.querySelector(".menu-button");
  const navMenu = document.querySelector(".nav-menu");

  menuButton.addEventListener("click", function () {
    navMenu.style.display = navMenu.style.display === "flex" ? "none" : "flex";
  });
});

// 페이지네이션 js --------------------------------------------------

// 페이지네이션 활성화
document
  .querySelectorAll(".pagination .page-item:not(.arrow)")
  .forEach((item) => {
    item.addEventListener("click", function () {
      document.querySelectorAll(".pagination .page-item").forEach((i) => {
        i.classList.remove("active");
      });
      this.classList.add("active");
    });
  });

// 모바일 메뉴 토글 기능 추가
document.querySelector(".menu-icon").addEventListener("click", function () {
  const navMenu = document.querySelector(".nav-menu");
  navMenu.style.display = navMenu.style.display === "flex" ? "none" : "flex";

  if (navMenu.style.display === "flex") {
    navMenu.style.position = "absolute";
    navMenu.style.top = "70px";
    navMenu.style.left = "0";
    navMenu.style.width = "100%";
    navMenu.style.flexDirection = "column";
    navMenu.style.backgroundColor = "#fff";
    navMenu.style.boxShadow = "0 2px 5px rgba(0,0,0,0.1)";
    navMenu.style.zIndex = "100";

    document.querySelectorAll(".nav-menu li").forEach((item) => {
      item.style.margin = "0";
      item.style.width = "100%";
      item.style.textAlign = "center";
      item.style.borderBottom = "1px solid #DBDAE2";
    });
  }
});

// 반응형 화면 크기 변경 감지
window.addEventListener("resize", function () {
  const navMenu = document.querySelector(".nav-menu");
  if (window.innerWidth > 992) {
    navMenu.style.display = "flex";
    navMenu.style.position = "absolute";
    navMenu.style.top = "auto";
    // navMenu.style.left = "50%";
    navMenu.style.width = "auto";
    navMenu.style.flexDirection = "row";
    navMenu.style.backgroundColor = "transparent";
    navMenu.style.boxShadow = "none";

    document.querySelectorAll(".nav-menu li").forEach((item) => {
      item.style.margin = "0 15px";
      item.style.width = "auto";
      item.style.textAlign = "center";
      item.style.borderBottom = "none";
    });
  } else {
    navMenu.style.display = "none";
  }
});
