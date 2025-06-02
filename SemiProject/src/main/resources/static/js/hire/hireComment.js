// REST(REpresentational State Transfer) API

// /comment (GET)
// /comment (POST)

// - 자원(데이터, 파일)을 주소로 구분하여(REpresentational)
// 자원의 상태(State)를 주고 받는 것(Transfer)

// -> 자원의 주소를 명시하고
//    HTTP Method(GET, POST, PUT, DELETE)를
//    이용해 지정된 자원에 대한 CRUD 진행행

// 현재 페이지의 전체 URL을 URL 객체로 파싱
const url = new URL(window.location.href); // 예: "http://localhost/hire/detail/40"

// URL 경로의 마지막 부분을 가져옴s
const id = url.pathname.split("/").pop();
console.log(id); // 40

// 이제 loginMember.memberNo 사용 가능
console.log(loginMemberNo);

const selectCommentList = () => {
  const data = {
    hireCommentContent: commentContent.value,
    hireNo: id,
  };
  fetch("/comment/select", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  })
    .then((resp) => resp.json())
    .then((hireCommentList) => {
      console.log(hireCommentList); // 데이터 확인
      const ul = document.querySelector("#commentList");
      ul.innerHTML = ""; // 기존 댓글 삭제

      hireCommentList.forEach((comment) => {
        console.log("parentNo:", comment.hireParentCommentNo, comment);
        const li = document.createElement("li");
        li.classList.add("comment-row");

        if (comment.hireParentCommentNo !== null) {
          li.classList.add("child-comment");
        }

        if (comment.hireCommentDelFl === "Y") {
          li.textContent = "삭제된 댓글 입니다";
        } else {
          // 작성자 영역
          const writerP = document.createElement("p");
          writerP.classList.add("comment-writer");

          // 프로필 영역
          const profile = document.createElement("p");
          profile.classList.add("porfile");

          // 내용 영역
          const content = document.createElement("p");
          content.classList.add("content");

          const profileImg = document.createElement("img");
          profileImg.src =
            comment.profileImg && comment.profileImg.trim() !== ""
              ? comment.profileImg
              : "/images/logo.png";
          profileImg.classList.add("comment-profile-img");

          // 클릭 시 openResumeModal 호출 (memberNo 전달)
          profileImg.onclick = () => openResumeModal(comment.memberNo);

          const nicknameSpan = document.createElement("span");
          nicknameSpan.classList.add("nickName");
          nicknameSpan.textContent = comment.memberNickname;

          profile.appendChild(profileImg);
          content.appendChild(nicknameSpan);

          if (
            loginMemberNo &&
            loginMemberNo !== comment.memberNo &&
            loginMemberNo === memberNo
          ) {
            const inviteBtn = document.createElement("button");
            inviteBtn.textContent = "초대";
            inviteBtn.classList.add("invite-btn");
            inviteBtn.onclick = () => {
              if (confirm(`${comment.memberNickname}님을 초대하시겠습니까?`)) {
                inviteMember(comment.memberNo, comment.memberNickname, id);
              }
            };

            content.append(inviteBtn);
          }

          // 댓글 내용
          const contentP = document.createElement("p");
          contentP.classList.add("comment-content");
          contentP.textContent = comment.hireCommentContent;

          content.appendChild(contentP);

          // 버튼 영역
          const btnDiv = document.createElement("div");
          btnDiv.classList.add("comment-btn-area");

          // 작성일
          const dateSpan = document.createElement("span");
          dateSpan.classList.add("comment-date");
          dateSpan.textContent = comment.hireCommentDate;

          const dateDiv = document.createElement("div");
          dateDiv.classList.add("date-wrapper"); // 필요하면 클래스 추가

          // span을 div 안에 넣기
          dateDiv.appendChild(dateSpan);
          btnDiv.append(dateDiv);

          const reDiv = document.createElement("div");
          reDiv.classList.add("btn-wrapper"); // 필요하면 클래스 추가

          const replyBtn = document.createElement("button");
          replyBtn.textContent = "답글";
          replyBtn.onclick = () =>
            showInsertComment(comment.hireCommentNo, replyBtn);
          reDiv.appendChild(replyBtn);

          // 로그인한 회원이 댓글 작성자일 때만 수정/삭제 버튼 노출
          if (loginMemberNo && loginMemberNo === comment.memberNo) {
            const updateBtn = document.createElement("button");
            updateBtn.textContent = "수정";
            updateBtn.onclick = () =>
              showUpdateComment(comment.hireCommentNo, updateBtn); // comment.hireCommentNo 사용

            const deleteBtn = document.createElement("button");
            deleteBtn.textContent = "삭제";
            deleteBtn.onclick = () =>
              deleteComment(comment.hireCommentNo, deleteBtn);

            reDiv.append(updateBtn, deleteBtn);
          }

          btnDiv.appendChild(reDiv);

          content.appendChild(btnDiv);

          writerP.append(profile, content);

          // 댓글 항목에 모두 추가
          li.append(writerP);
        }

        ul.appendChild(li);
      });
    })
    .catch((err) => console.error("댓글 목록 조회 실패:", err));
};

// 댓글 등록 (ajax)
const commentContent = document.querySelector("#commentContent"); // 댓글 작성하는 textarea
const addComment = document.querySelector("#addComment"); // 댓글 등록하는 button 태그

// 댓글 등록 버튼 클릭 시
addComment.addEventListener("click", (e) => {
  // 댓글 내용이 작성되지 않은 경우
  if (commentContent.value.trim().length == 0) {
    alert("내용 작성 후 등록버튼을 클릭해주세요.");
    commentContent.focus();
    return;
  }

  // ajax를 이용해 댓글 등록 요청
  const data = {
    hireCommentContent: commentContent.value,
    hireNo: id,
  };

  fetch("/comment", {
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
  // 수정창이 열려 있다면 닫기
  const existingUpdate = document.querySelector(".update-textarea");
  if (existingUpdate) {
    const confirmClose = confirm(
      "댓글 수정을 취소하고 답글을 작성하시겠습니까?"
    );
    if (!confirmClose) return;

    existingUpdate.closest("li").after(beforeCommentRow);
    existingUpdate.closest("li").remove();
  }

  // 다른 답글창이 열려 있다면 닫기
  const existingReply = document.querySelector(".commentInsertContent");
  if (existingReply) {
    const confirmClose = confirm(
      "다른 답글을 작성 중입니다. 현재 댓글에 답글을 작성 하시겠습니까?"
    );
    if (!confirmClose) return;

    existingReply.nextElementSibling.remove(); // 버튼 영역 제거
    existingReply.remove(); // textarea 제거
  }

  // 답글 textarea 생성
  const textarea = document.createElement("textarea");
  textarea.classList.add("commentInsertContent");
  btn.closest("li").appendChild(textarea);

  // 버튼 생성
  const commentBtnArea = document.createElement("div");
  commentBtnArea.classList.add("comment-btn-area1");
  const insertBtn = document.createElement("button");
  insertBtn.innerText = "등록";
  insertBtn.setAttribute(
    "onclick",
    "insertChildComment(" + parentCommentNo + ", this)"
  );
  const cancelBtn = document.createElement("button");
  cancelBtn.innerText = "취소";
  cancelBtn.setAttribute("onclick", "insertCancel(this)");
  commentBtnArea.append(insertBtn, cancelBtn);
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
    hireCommentContent: textarea.value,
    memberNo: loginMemberNo,
    hireNo: id,
    hireParentCommentNo: parentCommentNo,
  };

  fetch("/comment", {
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

/** 댓글 삭제
 *
 * @param commentNo (현재 댓글 번호)
 */

// 댓글 삭제 (ajax)
const deleteComment = (e) => {
  // 취소 선택 시
  if (!confirm("삭제하시겠습니까?")) return;

  fetch("/comment", {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
    body: e,
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
  // 답글창이 열려 있다면 닫기
  const existingReply = document.querySelector(".commentInsertContent");
  if (existingReply) {
    const confirmClose = confirm(
      "답글 작성을 취소하고 댓글을 수정하시겠습니까?"
    );
    if (!confirmClose) return;

    existingReply.nextElementSibling.remove();
    existingReply.remove();
  }

  // 수정창이 이미 열려 있다면 닫기
  const existingUpdate = document.querySelector(".update-textarea");
  if (existingUpdate) {
    const confirmClose = confirm("다른 댓글 수정을 취소하고 수정하시겠습니까?");
    if (!confirmClose) return;

    existingUpdate.closest("li").after(beforeCommentRow);
    existingUpdate.closest("li").remove();
  }

  // 수정 textarea 생성
  const commentRow = btn.closest("li");
  beforeCommentRow = commentRow.cloneNode(true);
  const contentP = commentRow.querySelector(".comment-content");
  const beforeContent = contentP ? contentP.textContent : "";

  if (contentP) contentP.remove();

  const textarea = document.createElement("textarea");
  textarea.classList.add("update-textarea");
  textarea.value = beforeContent;
  commentRow.appendChild(textarea);

  const commentBtnArea = document.createElement("div");
  commentBtnArea.classList.add("comment-btn-area1");

  const updateBtn = document.createElement("button");
  updateBtn.innerText = "수정";
  updateBtn.setAttribute("onclick", `updateComment(${commentNo}, this)`);

  const cancelBtn = document.createElement("button");
  cancelBtn.innerText = "취소";
  cancelBtn.setAttribute("onclick", "updateCancel(this)");

  commentBtnArea.append(updateBtn, cancelBtn);
  commentRow.appendChild(commentBtnArea);
};
// --------------------------------------------------------------------
/** 댓글 수정 취소
 * @param {*} btn : 취소 버튼
 */
const updateCancel = (btn) => {
  if (confirm("취소 하시겠습니까?")) {
    selectCommentList();
  }
};

// 댓글 수정 fetch 요청
const updateComment = (e, btn) => {
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
    hireCommentNo: e,
    hireCommentContent: textarea.value,
  };

  fetch("/comment", {
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

document.addEventListener("DOMContentLoaded", function () {
  selectCommentList(); // DOM이 완전히 로드된 후 실행
});

function inviteMember(memberNo, memberNickname, hireNo) {
  console.log("초대 멤버 번호:", memberNo);
  console.log("초대 멤버 닉네임:", memberNickname);

  fetch(`/hire/invite/${hireNo}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ memberNo: memberNo }),
  })
    .then((res) => res.text())
    .then((msg) => alert(`${memberNickname}님${msg}`));
}
