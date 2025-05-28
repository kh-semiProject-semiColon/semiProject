// ---------------- 게시글 수정 버튼 ----------------------

const updateBtn = document.querySelector("#updateBtn");

if (updateBtn != null) {
  updateBtn.addEventListener("click", () => {
    console.log(location.pathname);
    // get 방식
    // 현재 : /board/1/2001?cp=1
    // 목표 : /editBoard/1/2001/update?cp=1
    const newPath =
      location.pathname.replace("/board/", "/editBoard/") + "/update";
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

    const url = location.pathname.replace("board", "editBoard") + "/delete";
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
