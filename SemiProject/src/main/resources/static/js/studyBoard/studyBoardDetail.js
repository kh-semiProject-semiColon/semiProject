// 좋아요 버튼(하트) 클릭 시 비동기로 좋아요 INSERT/DELETE

// 타임리프 코드 해석 순서
// 1. th : 코드(java) + Spring EL
// 2. html 코드 ( + css / js)

// 1) 로그인한 회원번호 준비
// memberNo -> 현재 로그인한 사람의 memberNo

// 2) 현재 게시글 번호 준비
// boardNo -> 현재 게시글의 번호

// 3) 조항요 여부 준비비
// likeCheck -> 현재 게시글의 likeCheck 값
// -> 1이면 DELETE(취소)를 위한 sql 필요 / 0이면 INSERT(좋아요)를 위한 sql 필요요

// 1. #boardLike가 클릭 되었을 때
document.querySelector("#boardLike").addEventListener("click", (e) => {
  // 2. 로그인 상태가 아닌 경우 동작 X
  if (loginMemberNo == null) {
    alert("로그인 후 이용해주세요.");
    return;
  }

  // 3. 준비된 3개의 변수를 객체로 저장(JSON으로 서버 제출)
  const obj = {
    memberNo: loginMemberNo,
    studyBoardNo: boardNo,
    likeCheck: likeCheck,
  };

  // 4. 좋아요 INSERT/DELETE 비동기 요청
  fetch("/studyBoard/like", {
    method: "post",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj),
  })
    .then((resp) => resp.text())
    .then((count) => {
      if (count == -1) {
        console.log("좋아요 처리 실패");
        return;
      }

      // 5. likeCheck 값 0 <-> 1 변환
      // -> 클릭 될 때마다 INSERT / DELETE 동작을 번갈아 가면서 하게끔
      likeCheck = likeCheck == 0 ? 1 : 0;

      // 6. 하트를 채웠다 비웠다 바꾸기
      e.target.classList.toggle("fa-regular"); // 빈 하트
      e.target.classList.toggle("fa-solid"); // 채워진 하트

      // 7. 게시글 좋아요 수 수정
      e.target.nextElementSibling.innerText = count;
    });
});

// ---------------- 게시글 수정 버튼 ----------------------

const updateBtn = document.querySelector("#updateBtn");

if (updateBtn != null) {
  updateBtn.addEventListener("click", () => {
    console.log(location.pathname);
    // get 방식
    // 현재 : /board/1/2001?cp=1
    // 목표 : /editBoard/1/2001/update?cp=1
    const newPath =
      location.pathname.replace("/studyBoard/", "/editBoard/studyBoard/") +
      "/update";
    console.log("이동할 경로:", newPath);
    location.href = newPath + location.search;
  });
}

// 삭제
const deleteBtn2 = document.querySelector("#deleteBtn2");

if (deleteBtn2 != null) {
  deleteBtn2.addEventListener("click", () => {
    if (!confirm("삭제 하시겠습니까?")) {
      alert("취소되었습니다");
      return;
    }

    const url =
      location.pathname.replace("studyBoard", "editBoard/studyBoard") +
      "/delete";
    // 목표 : /editBoard/1/2004/delete?cp=1

    // JS에서 동기식으로 Post 요청 보내는 법법
    // -> form 태그 생성
    const form = document.createElement("form");
    form.action = url;
    form.method = "POST";

    // 쿼리스트링으로 값을 보내는 것은 GET방식
    // POST 방식은 name 속성값으로 전송해야함
    // cp 값을 저장할 input 태그 생성
    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "cp";

    // 쿼리스트링에서 원하는 파라미터 얻어오기
    const params = new URLSearchParams(location.search);
    // cp=1
    const cp = params.get("cp"); // 1
    input.value = cp;

    form.append(input);

    // 화면에 form 태그 추가 후 제출하기
    document.querySelector("body").append(form);
    form.submit();
  });
}
