const cardData = Array.from({ length: 12 }, () => ({
  title: `(번역) 'use client'은 무엇을 하나요?`,
  desc: `"use client"는 React의 클라이언트 렌더링 지시자입니다.`,
  author: "sehyun hwang",
  likes: 10,
  image: "/images/test.jpg",
}));

const container = document.getElementById("cardContainer");

cardData.forEach((data) => {
  const card = document.createElement("div");
  card.className = "card";
  card.innerHTML = `
        <img src="${data.image}" class="card-image" />
        <div class="card-content">
          <p class="card-text">${data.title}</p>
          <p class="card-description">${data.desc}</p>
		  <div class="card-divider"></div>
          <div class="card-footer">
            <span>${data.author}</span>
            <span>❤️ ${data.likes}</span>
          </div>
        </div>
      `;
  container.appendChild(card);
});
