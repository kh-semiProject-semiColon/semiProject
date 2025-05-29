// 프로필 조회 js --------------------------------------------------
document.addEventListener("DOMContentLoaded", function() {
	// 닉네임 input창 요소
	const memberNickname = document.querySelector("[name=memberNickname]");
	// 닉네임 span 요소
	const nickNameMessage = document.querySelector("#nickNameMessage");
	// 전화번호 input창 요소
	const memberTel = document.querySelector("[name=memberTel]");
	// 전화번호 span 요소
	const TelMessage = document.querySelector("#TelMessage");
	// 주소 검색 버튼 기능
	const addressSearchBtn = document.querySelector(".address-search");
	// 프로필 사진 x버튼 (삭제)
	const removeImageBtn = document.querySelector(".remove-image");
	// 이미지 선택 버튼
	const imageSelectBtn = document.querySelector(".image-select-btn");
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

	// // 이미지 선택 버튼 기능
	// imageSelectBtn.addEventListener("click", function () {
	//   const fileInput = document.createElement("input");
	//   fileInput.type = "file";
	//   fileInput.accept = "image/*";
	//   fileInput.click();

	//   fileInput.addEventListener("change", function () {
	//     if (this.files && this.files[0]) {
	//       const reader = new FileReader();
	//       reader.onload = function (e) {
	//         profileImage.src = e.target.result;
	//       };
	//       reader.readAsDataURL(this.files[0]);
	//     }
	//   });
	// });

	// 다음 주소 API 다루기
	function execDaumPostcode() {
		new daum.Postcode({
			oncomplete: function(data) {
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

	// -----------------------------------------------------------
	// 이미지 업로드 구간

	/*  [input type="file" 사용 시 유의 사항]
	
	  1. 파일 선택 후 취소를 누르면 
		선택한 파일이 사라진다  (value == '')
	
	  2. value로 대입할 수 있는 값은  '' (빈칸)만 가능하다
	
	  3. 선택된 파일 정보를 저장하는 속성은
		value가 아니라 files이다
	*/

	// 요소 참조
	const profileForm = document.getElementById("profile");  // 프로필 form

	if (profileForm != null) {

		const profileImg = document.getElementById("profileImg");  // 미리보기 이미지 img
		const imageInput = document.getElementById("imageInput");  // 이미지 파일 선택 input
		const MAX_SIZE = 1024 * 1024 * 5;  // 최대 파일 크기 설정 (5MB)


		// 절대경로 방식을 기본 이미지 URL 설정
		const defaultImageUrl = `${window.location.origin}/images/default-profile.png`;
		// http://localhost/ == ${window.location.origin}
		// http://localhost/images/user.png

		let statusCheck = -1; // -1 : 초기상태, 0 : 이미지 삭제, 1: 새 이미지 선택

		// img 태그에 작성하는 값 src = 미리보기 이미지를 띄울 URL 주소
		let previousImage = profileImg.src;  // 이전 이미지 URL 기록(초기 상태의 이미지 URL 저장)

		// input (type=file) 태그가 작성할 값 = 서버에 실제로 제출되는 File 객체
		let previousFile = null; // 이전에 선택된 파일 객체를 저장

		imageSelectBtn.addEventListener("click", () => {
			imageInput.click();
		});

		// 이미지 선택 시 미리보기 및 파일 크기 검사

		imageInput.addEventListener("change", () => {
			// change 이벤트 : 기존에 있던 값과 달라지면 발생되는 이벤트
			// console.log(imageInput.files); // FileList 

			const file = imageInput.files[0]; // 실제로 선택한 File 객체 가져오기
			console.log(file);

			if (file) { // 파일이 선택된 경우

				if (file.size <= MAX_SIZE) { // 현재 선택한 파일의 크기가 허용 범위 이내인 경우 (정상인 경우)
					const newImageUrl = URL.createObjectURL(file);  // 임시 URL 생성
					//  URL.createObjectURL(파일) : 웹에서 접근 가능한 임시 URL 반환
					console.log(newImageUrl);
					profileImg.src = newImageUrl; // 미리보기 이미지 설정
					// (img 태그의 src에 선택한 파일 임시 경로 대입)
					previousImage = newImageUrl; // 현재 선택된 이미지를 이전 이미지로 저장 (다음에 바뀔 일에 대비)
					previousFile = file; // 현재 선택된 파일 객체를 이전 파일로 저장 (다음에 바뀔 일에 대비)
					statusCheck = 1; // 새 이미지 선택 상태 기록

				} else { // 파일 크기가 허용범위를 초과한 경우
					alert("5MB 이하의 이미지를 선택해주세요.");
					imageInput.value = ""; // 1. 파일 선택 초기화
					profileImg.src = previousImage; // 2. 이전 미리보기 이미지로 복원

					// 3. 파일 입력 복구 : 이전 파일이 존재하면 다시 할당
					if (previousFile != null) {
						const dataTransfer = new dataTransfer();
						dataTransfer.items.add(previousFile);
						imageInput.files = dataTransfer.files;
					}


				}



			} else { // 파일이 선택되지 않은 경우 (== 취소를 누른 경우)

				// 이전 미리보기 이미지로 복원 (img)
				profileImg.src = previousImage;

				// 이전 선택한 파일로 복원 (input)
				if (previousFile) { // 이전 파일이 존재한다면 
					const dataTransfer = new DataTransfer();
					// -> DataTransfer : 자바스크립트로 파일을 조작할 때 사용되는 인터페이스 
					// DataTransfer.items.add() : 파일 추가
					// DataTransfer.items.remove() : 파일 제거
					// DataTransfer.files : FileList 객체를 반환
					// -> <input type="file"> 요소에 파일을 동적으로 설정.
					// --> input 태그의 files 속성은 FileList만 저장 가능한 형태이기 때문에
					// DataTransfer를 이용하여 현재 File 객체를 FileList 변환하여 할당 진행

					dataTransfer.items.add(previousFile);
					imageInput.files = dataTransfer.files;  // FileList 반환
				}
			}
		});
		// 이미지 삭제 버튼 클릭 시 
		removeImageBtn.addEventListener("click", () => {

			// 기본 이미지 상태가 아니면 삭제 처리
			if (profileImg.src !== defaultImageUrl) {
				imageInput.value = "";  // input 태그 파일값 초기화
				profileImg.src = defaultImageUrl;  // 현재 미리보기를 기본 이미지로 변경
				statusCheck = 0; // 삭제 상태 기록
				previousImage = defaultImageUrl; // 이전 이미지 기록하는 변수에 기본이미지로 변경
				previousFile = null;   // 이전 파일 기록하는 변수에 null로 초기화

			} else { // 기본 이미지 상태에서 삭제 버튼 클릭 시 상태를 변경하지 않음
				statusCheck = -1; // 변경사항 없음 상태 유지

			}
		});

		// 폼 제출 시 유효성 검사
		profileForm.addEventListener("submit", e => {
			if (statusCheck === -1) { // 변경 사항이 없는 경우 제출 막기
				e.preventDefault();
				alert("변경사항이 없습니다.");
			}
		});
	}


});
