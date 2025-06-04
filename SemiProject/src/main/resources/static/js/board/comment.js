// REST(REpresentational State Transfer) API

// /comments (GET)
// /comments (POST)

// - 자원(데이터, 파일)을 주소로 구분하여(REpresentational)
// 자원의 상태(State)를 주고 받는 것(Transfer)

// -> 자원의 주소를 명시하고
//    HTTP Method(GET, POST, PUT, DELETE)를
//    이용해 지정된 자원에 대한 CRUD 진행행

const selectCommentList = () => {
  fetch("/comments?boardNo=" + boardNo)
    .then((resp) => resp.json())
    .then((commentList) => {
      const ul = document.querySelector("#commentList");
      ul.innerHTML = ""; // 기존 댓글 목록 삭제

      for (let comment of commentList) {
        const commentRow = document.createElement("li");
        commentRow.classList.add("comment-row");
        if (comment.parentCommentNo != 0)
          commentRow.classList.add("child-comment");

        if (comment.commentDelFl === "Y") {
          commentRow.innerText = "삭제된 댓글 입니다";
        } else {
          // ----- 댓글 작성자 영역 -----
          const commentWriter = document.createElement("div");
          commentWriter.classList.add("comment-writer");

          const proNick = document.createElement("div");
          proNick.classList.add("proNick");

          const profileImg = document.createElement("img");
          profileImg.src = comment.profileImg || userDefaultIamge;
          proNick.appendChild(profileImg);

          const nickname = document.createElement("span");
          nickname.innerText = comment.memberNickname;
          proNick.appendChild(nickname);

          // 버튼 영역
          const commentBtnArea = document.createElement("div");
          commentBtnArea.classList.add("comment-btn-area");
          const btnArea = document.createElement("div");
          btnArea.classList.add("btn-area");

          const replyBtn = document.createElement("button");
          replyBtn.innerText = "답글";
          replyBtn.setAttribute(
            "onclick",
            `showInsertComment(${comment.commentNo}, this)`
          );
          btnArea.appendChild(replyBtn);

          commentBtnArea.append(btnArea);

          if (loginMemberNo != null && loginMemberNo == comment.memberNo) {
            const updateBtn = document.createElement("button");
            updateBtn.innerText = "수정";
            updateBtn.setAttribute(
              "onclick",
              `showUpdateComment(${comment.commentNo}, this)`
            );

            const deleteBtn = document.createElement("button");
            deleteBtn.innerText = "삭제";
            deleteBtn.setAttribute(
              "onclick",
              `deleteComment(${comment.commentNo})`
            );

            btnArea.append(updateBtn, deleteBtn);
          }

          commentWriter.append(proNick);

          // ----- 댓글 내용 -----
          const contentWrapper = document.createElement("div");
          contentWrapper.classList.add("content-comment");

          const content = document.createElement("p");
          content.classList.add("comment-content");
          content.innerText = comment.commentContent;

          contentWrapper.appendChild(content);

          // ----- 댓글 작성일 -----
          const commentDate = document.createElement("span");
          commentDate.classList.add("comment-date");
          commentDate.innerText = comment.commentWrittenDate;

          // ----- 조립 -----
          commentRow.append(commentWriter, contentWrapper);
          contentWrapper.after(commentBtnArea);
          commentBtnArea.prepend(commentDate);
        }

        ul.appendChild(commentRow);
      }
    });
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
  // 기존 답글 작성 textarea 모두 제거하는 함수
  const removeExistingReplyAreas = () => {
    const existingTextareas = document.querySelectorAll(
      ".commentInsertContent"
    );
    existingTextareas.forEach((ta) => {
      const btnArea = ta.nextElementSibling;
      if (btnArea && btnArea.classList.contains("comment-btn-area")) {
        btnArea.remove();
      }
      ta.remove();
    });
  };

  if (document.querySelector(".commentInsertContent")) {
    if (
      confirm(
        "다른 답글을 작성 중입니다. 현재 댓글에 답글을 작성 하시겠습니까?"
      )
    ) {
      removeExistingReplyAreas();
    } else {
      return;
    }
  }

  const commentLi = btn.closest("li.comment-row");

  const replyComment = document.createElement("div");

  const textarea = document.createElement("textarea");
  textarea.classList.add("commentInsertContent");
  replyComment.append(textarea);

  const commentBtnArea = document.createElement("div");
  commentBtnArea.classList.add("comment-btn-area");
  commentBtnArea.classList.add("reply");

  const insertBtn = document.createElement("button");
  insertBtn.innerText = "등록";
  insertBtn.setAttribute(
    "onclick",
    `insertChildComment(${parentCommentNo}, this)`
  );

  const cancelBtn = document.createElement("button");
  cancelBtn.innerText = "취소";
  cancelBtn.setAttribute("onclick", "insertCancel(this)");

  commentBtnArea.append(insertBtn, cancelBtn);
  textarea.after(commentBtnArea);

  commentLi.after(replyComment);
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
        console.log(loginMemberNo);

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
  if (temp != null) {
    if (confirm("수정 중인 댓글이 있습니다. 현재 댓글을 수정 하시겠습니까?")) {
      const commentRow = temp.closest("li");
      commentRow.after(beforeCommentRow);
      commentRow.remove();
    } else {
      return;
    }
  }

  const commentRow = btn.closest("li");
  beforeCommentRow = commentRow.cloneNode(true);

  const contentDiv = commentRow.querySelector(".content-comment");
  const beforeContent = contentDiv.innerText;

  contentDiv.innerHTML = "";
  const textarea = document.createElement("textarea");
  textarea.classList.add("update-textarea", "comment-content");
  textarea.value = beforeContent;
  contentDiv.appendChild(textarea);

  const btnArea = commentRow.querySelector(".comment-btn-area");
  btnArea.replaceChildren();

  const updateBtn = document.createElement("button");
  updateBtn.innerText = "수정";
  updateBtn.onclick = () => updateComment(commentNo, updateBtn);

  const cancelBtn = document.createElement("button");
  cancelBtn.innerText = "취소";
  cancelBtn.onclick = () => updateCancel(cancelBtn);
  btnArea.append(updateBtn, cancelBtn);

  btnArea.classList.add("reply-btn-area");
  btnArea.classList.remove("btn-area");
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

const updateComment = (commentNo, btn) => {
  const commentRow = btn.closest("li");
  const textarea = commentRow.querySelector(".update-textarea");

  if (textarea.value.trim().length == 0) {
    alert("댓글 작성 후 수정 버튼을 클릭해주세요.");
    textarea.focus();
    return;
  }

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

        // 1) 댓글 내용 영역 (textarea -> p.comment-content)
        const contentDiv = commentRow.querySelector(".content-comment");
        contentDiv.innerHTML = ""; // 기존 textarea 삭제

        const p = document.createElement("p");
        p.classList.add("comment-content");
        p.innerText = textarea.value;
        contentDiv.appendChild(p);

        // 2) 버튼 영역 복원
        const btnArea = commentRow.querySelector(".comment-btn-area");
        btnArea.innerHTML = "";

        // 답글 버튼
        const childCommentBtn = document.createElement("button");
        childCommentBtn.innerText = "답글";
        childCommentBtn.setAttribute(
          "onclick",
          `showInsertComment(${commentNo}, this)`
        );
        btnArea.appendChild(childCommentBtn);

        // 작성자 번호 확인 후 수정/삭제 버튼 추가
        // (commentRow 에 data-member-no 속성으로 작성자 번호가 저장되어 있다고 가정)
        const updateBtn = document.createElement("button");
        updateBtn.innerText = "수정";
        updateBtn.setAttribute(
          "onclick",
          `showUpdateComment(${commentNo}, this)`
        );

        const deleteBtn = document.createElement("button");
        deleteBtn.innerText = "삭제";
        deleteBtn.setAttribute("onclick", `deleteComment(${commentNo})`);

        btnArea.append(updateBtn, deleteBtn);
        selectCommentList();
      } else {
        selectCommentList();
        alert("댓글 수정이 실패하였습니다");
      }
    })
    .catch((err) => {
      console.error(err);
      selectCommentList();
      alert("댓글 수정 중 오류가 발생했습니다.");
    });
};
