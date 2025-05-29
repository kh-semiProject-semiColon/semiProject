// REST(REpresentational State Transfer) API

// /comments (GET)
// /comments (POST)

// - 자원(데이터, 파일)을 주소로 구분하여(REpresentational)
// 자원의 상태(State)를 주고 받는 것(Transfer)

// -> 자원의 주소를 명시하고
//    HTTP Method(GET, POST, PUT, DELETE)를
//    이용해 지정된 자원에 대한 CRUD 진행행

// 댓글 목록 조회(비동기 / ajax)
const selectCommentList = () => {
  // [GET]
  // fetch(주소?쿼리스트링)

  // [POST, PUT, DELETE]
  // fetch(주소, {method, header, boady 작성})

  const selectCommentList = () => {
    fetch("/comment?hireNo=" + hireNo)
      .then((resp) => resp.json())
      .then((commentList) => {
        console.log(commentList);

        const ul = document.querySelector("#CommentList");
        ul.innerHTML = ""; // 기존 댓글 삭제

        commentList.forEach((comment) => {
          const li = document.createElement("li");
          li.classList.add("comment-row");

          if (comment.parentCommentNo !== 0) {
            li.classList.add("child-comment");
          }

          if (comment.commentDelFl === "Y") {
            li.textContent = "삭제된 댓글 입니다";
          } else {
            // 작성자 영역
            const writerP = document.createElement("p");
            writerP.classList.add("comment-writer");

            const profileImg = document.createElement("img");
            profileImg.src = comment.profileImg || userDefaultImage;

            const nicknameSpan = document.createElement("span");
            nicknameSpan.textContent = comment.memberNickname;

            const dateSpan = document.createElement("span");
            dateSpan.classList.add("comment-date");
            dateSpan.textContent = comment.commentWrittenDate;

            writerP.append(profileImg, nicknameSpan, dateSpan);

            // 댓글 내용
            const contentP = document.createElement("p");
            contentP.classList.add("comment-content");
            contentP.textContent = comment.commentContent;

            // 버튼 영역
            const btnDiv = document.createElement("div");
            btnDiv.classList.add("comment-btn-area");

            const replyBtn = document.createElement("button");
            replyBtn.textContent = "답글";
            replyBtn.onclick = () =>
              showInsertComment(comment.commentNo, replyBtn);
            btnDiv.appendChild(replyBtn);

            // 로그인한 회원이 댓글 작성자일 때만 수정/삭제 버튼 노출
            if (loginMemberNo && loginMemberNo === comment.memberNo) {
              const updateBtn = document.createElement("button");
              updateBtn.textContent = "수정";
              updateBtn.onclick = () =>
                showUpdateComment(comment.commentNo, updateBtn);

              const deleteBtn = document.createElement("button");
              deleteBtn.textContent = "삭제";
              deleteBtn.onclick = () => deleteComment(comment.commentNo);

              btnDiv.append(updateBtn, deleteBtn);
            }

            // 댓글 항목에 모두 추가
            li.append(writerP, contentP, btnDiv);
          }

          ul.appendChild(li);
        });
      })
      .catch((err) => console.error("댓글 목록 조회 실패:", err));
  };
};

// 댓글 등록 (ajax)
const commentContent = document.querySelector("#commentContent"); // 댓글 작성하는 textarea
const addComment = document.querySelector("#addComment"); // 댓글 등록하는 button 태그

// 댓글 등록 버튼 클릭 시
addComment.addEventListener("click", (e) => {
  // 로그인 여부 확인
  console.log("dd");
  if (loginMemberNo == null) {
    alert("로그인 후 이용해주세요.");
    return;
  }

  // 댓글 내용이 작성되지 않은 경우
  if (commentContent.value.trim().length == 0) {
    alert("내용 작성 후 등록버튼을 클릭해주세요.");
    commentContent.focus();
    return;
  }

  // ajax를 이용해 댓글 등록 요청
  const data = {
    commentContent: commentContent.value,
    memberNo: loginMemberNo,
    boardNo: boardNo,
  };

  fetch("/comments", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  })
    .then((resp) => resp.text())
    .then((result) => {
      if (result > 0) {
        alert("댓글이 등록되었습니다");
        selectCommentList();
        commentContent.value = ""; // 작성한 댓글 지우기
      } else {
        alert("댓글이 등록하는데 실패하였습니다");
      }
    })
    .catch((err) => console.log(err));
});

/** 답글 작성 화면 추가
 * @param {*} parentCommentNo
 * @param {*} btn
 */
const showInsertComment = (parentCommentNo, btn) => {
  // ** 답글 작성 textarea가 한 개만 열릴 수 있도록 만들기 **
  const temp = document.getElementsByClassName("commentInsertContent");
  if (temp.length > 0) {
    // 답글 작성 textara가 이미 화면에 존재하는 경우
    if (
      confirm(
        "다른 답글을 작성 중입니다. 현재 댓글에 답글을 작성 하시겠습니까?"
      )
    ) {
      temp[0].nextElementSibling.remove(); // 버튼 영역부터 삭제
      temp[0].remove(); // textara 삭제 (기준점은 마지막에 삭제해야 된다!)
    } else {
      return; // 함수를 종료시켜 답글이 생성되지 않게함.
    }
  }
  // 답글을 작성할 textarea 요소 생성
  const textarea = document.createElement("textarea");
  textarea.classList.add("commentInsertContent");
  // 답글 버튼의 부모의 뒤쪽에 textarea 추가
  // after(요소) : 뒤쪽에 추가
  btn.parentElement.after(textarea);
  // 답글 버튼 영역 + 등록/취소 버튼 생성 및 추가
  const commentBtnArea = document.createElement("div");
  commentBtnArea.classList.add("comment-btn-area");
  const insertBtn = document.createElement("button");
  insertBtn.innerText = "등록";

  // 매개변수에 +문자열+ 작성시 Number타입으로 형변환이 진행된다
  // == +parentCommentNo+ == Number(parentCommentNo)
  insertBtn.setAttribute(
    "onclick",
    "insertChildComment(" + parentCommentNo + ", this)"
  );
  const cancelBtn = document.createElement("button");
  cancelBtn.innerText = "취소";
  cancelBtn.setAttribute("onclick", "insertCancel(this)");
  // 답글 버튼 영역의 자식으로 등록/취소 버튼 추가
  commentBtnArea.append(insertBtn, cancelBtn);
  // 답글 버튼 영역을 화면에 추가된 textarea 뒤쪽에 추가
  textarea.after(commentBtnArea);
};
// ---------------------------------------
/** 답글 (자식 댓글) 작성 취소
 * @param {*} cancelBtn : 취소 버튼
 */
const insertCancel = (cancelBtn) => {
  // 취소 버튼 부모의 이전 요소(textarea) 삭제
  cancelBtn.parentElement.previousElementSibling.remove();
  // 취소 버튼이 존재하는 버튼영역 삭제
  cancelBtn.parentElement.remove();
};

const insertChildComment = (parentCommentNo, btn) => {
  // 답글 내용이 작성된 textarea 요소
  const textarea = btn.parentElement.previousElementSibling;

  // 유효성 검사
  if (textarea.value.trim().length == 0) {
    alert("내용 작성 후 등록 버튼을 클릭해주세요");
    textarea.focus();
    return;
  }

  // ajax를 통한 답글 작성
  const data = {
    commentContent: textarea.value,
    memberNo: loginMemberNo,
    boardNo: boardNo,
    parentCommentNo: parentCommentNo,
  };

  fetch("/comments", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  })
    .then((resp) => resp.text())
    .then((result) => {
      if (result > 0) {
        alert("답글이 등록되었습니다");
        selectCommentList();
      } else {
        if (loginMemberNo == null) {
          alert("로그인 후 작성해주세요.");

          return;
        }
        alert("답글 등록이 실패하였습니다");
      }
    });
};

/** 댓글 삭제제
 *
 * @param commentNo (현재 댓글 번호)
 */

// 댓글 삭제 (ajax)
const deleteComment = (commentNo) => {
  // 취소 선택 시
  if (!confirm("삭제하시겠습니까?")) return;

  fetch("/comments", {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
    body: commentNo,
  })
    .then((resp) => resp.text())
    .then((result) => {
      if (result > 0) {
        alert("삭제 되었습니다.");
        selectCommentList();
      } else {
        alert("삭제에 실패하였습니다.");
      }
    });
};

// 댓글 수정 (ajax)

// 수정 취소 시 원래 댓글 형태로 돌아가기 위한 백업 변수
let beforeCommentRow;
/** 댓글 수정 화면 전환
 * @param {*} commentNo
 * @param {*} btn
 */
const showUpdateComment = (commentNo, btn) => {
  /* 댓글 수정 화면이 1개만 열릴 수 있게 하기 */
  const temp = document.querySelector(".update-textarea");
  // .update-textarea 존재 == 열려있는 댓글 수정창이 존재
  if (temp != null) {
    if (confirm("수정 중인 댓글이 있습니다. 현재 댓글을 수정 하시겠습니까?")) {
      const commentRow = temp.parentElement; // 기존 댓글 행
      commentRow.after(beforeCommentRow); // 기존 댓글 다음에 백업 추가
      commentRow.remove(); // 기존 삭제 -> 백업이 기존 행 위치로 이동
    } else {
      // 취소
      return;
    }
  }
  // -------------------------------------------
  // 1. 댓글 수정이 클릭된 행 (.comment-row) 선택
  const commentRow = btn.closest("li");
  // 2. 행 전체를 백업(복제)
  // 요소.cloneNode(true) : 요소 복제,
  //           매개변수 true == 하위 요소도 복제
  beforeCommentRow = commentRow.cloneNode(true);
  // console.log(beforeCommentRow);
  // 3. 기존 댓글에 작성되어 있던 내용만 얻어오기
  let beforeContent = commentRow.children[1].innerText;
  // 4. 댓글 행 내부를 모두 삭제
  commentRow.innerHTML = "";
  // 5. textarea 생성 + 클래스 추가 + 내용 추가
  const textarea = document.createElement("textarea");
  textarea.classList.add("update-textarea");
  textarea.value = beforeContent;
  // 6. 댓글 행에 textarea 추가
  commentRow.append(textarea);
  // 7. 버튼 영역 생성
  const commentBtnArea = document.createElement("div");
  commentBtnArea.classList.add("comment-btn-area");
  // 8. 수정 버튼 생성
  const updateBtn = document.createElement("button");
  updateBtn.innerText = "수정";
  updateBtn.setAttribute("onclick", `updateComment(${commentNo}, this)`);
  // 9. 취소 버튼 생성
  const cancelBtn = document.createElement("button");
  cancelBtn.innerText = "취소";
  cancelBtn.setAttribute("onclick", "updateCancel(this)");
  // 10. 버튼 영역에 수정/취소 버튼 추가 후
  //     댓글 행에 버튼 영역 추가
  commentBtnArea.append(updateBtn, cancelBtn);
  commentRow.append(commentBtnArea);
};
// --------------------------------------------------------------------
/** 댓글 수정 취소
 * @param {*} btn : 취소 버튼
 */
const updateCancel = (btn) => {
  if (confirm("취소 하시겠습니까?")) {
    const commentRow = btn.closest("li"); // 기존 댓글 행
    commentRow.after(beforeCommentRow); // 기존 댓글 다음에 백업 추가
    commentRow.remove(); // 기존 삭제 -> 백업이 기존 행 위치로 이동
  }
};

// 댓글 수정 fetch 요청
const updateComment = (commentNo, btn) => {
  // 수정된 내용이 작성된 textarea 얻어오기
  const textarea = btn.parentElement.previousElementSibling;

  // 유효성 검사
  if (textarea.value.trim().length == 0) {
    alert("댓글 작성 후 수정 버튼을 클릭해주세요.");
    textarea.focus();
    return;
  }

  // 수정 시 전달 데이터
  const data = {
    commentNo: commentNo,
    commentContent: textarea.value,
  };

  fetch("/comments", {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  })
    .then((resp) => resp.text())
    .then((result) => {
      if (result > 0) {
        alert("댓글 수정이 되었습니다");
        selectCommentList();
      } else {
        alert("댓글 수정이 실패하였습니다");
      }
    });
};
