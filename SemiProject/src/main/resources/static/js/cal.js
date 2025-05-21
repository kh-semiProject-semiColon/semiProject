$(document).ready(function () {
  let selectedColor = "#3788d8"; // 기본 색상

  let calendarTag = $("#calendar");
  let calendar = new FullCalendar.Calendar(calendarTag[0], {
    height: "550px",
    expandRows: true,
    slotMinTime: "00:00",
    slotMaxTime: "23:59",
    customButtons: {
      colorPickerButton: {
        text: "색상",
        click: function () {}, // 클릭 없어도 OK
      },
    },
    headerToolbar: {
      left: "prevYear,prev,next,nextYear today",
      center: "title",
      right: "colorPickerButton dayGridMonth",
    },
    initialView: "dayGridMonth",
    displayEventTime: false,
    editable: true,
    selectable: true,
    eventStartEditable: true,
    eventDurationEditable: true,
    navLinks: true,
    nowIndicator: true,
    dayMaxEvents: 2,
    locale: "ko",

    events: "/calendarList",

    select: function (arg) {
      let title = prompt("일정 입력");
      if (title) {
        let newData = {
          title: title,
          start: arg.start,
          end: arg.end,
          allDay: true,
          color: selectedColor,
        };

        $.ajax({
          url: "/calendarSave",
          method: "POST",
          dataType: "json",
          data: JSON.stringify(newData),
          contentType: "application/json",
          success: function (data) {
            if (data != null) {
              calendar.addEvent({
                id: data.calendarNo,
                title: data.title,
                start: data.start1.substring(0, 10),
                end: data.end.substring(0, 10),
                allDay: true,
                editable: true,
                color: selectedColor,
              });
            }
          },
        });
      }
      calendar.unselect();
    },

    eventClick: function (arg) {
      if (confirm("선택한 일정을 삭제하시겠습니까?")) {
        $.ajax({
          type: "DELETE",
          url: "/calendarDelete/" + arg.event.id,
          data: { no: arg.event.id },
          success: function (data) {
            if (data === "success") {
              alert("삭제하였습니다.");
              arg.event.remove();
            } else {
              alert("오류가 발생하였습니다");
            }
          },
        });
      }
    },

    eventDrop: function (arg) {
      updateEvent(arg);
    },

    eventResize: function (arg) {
      updateEvent(arg);
    },
  });

  calendar.render();

  // 버튼 렌더링 후 select 삽입
  setTimeout(() => {
    const btn = document.querySelector(".fc-colorPickerButton-button");
    if (btn && !document.getElementById("colorPicker")) {
      const select = document.createElement("select");
      select.id = "colorPicker";
      select.style.marginLeft = "10px";
      select.style.height = "26px";
      select.innerHTML = `
        <option value="#3788d8">파랑</option>
        <option value="#28a745">초록</option>
        <option value="#dc3545">빨강</option>
        <option value="#ffc107">노랑</option>
        <option value="#25272B">검정</option>
      `;
      btn.innerHTML = ""; // 기존 텍스트 제거
      btn.appendChild(select);

      // 색상 변경 이벤트
      select.addEventListener("change", function () {
        selectedColor = this.value;
      });
    }
  }, 100); // 약간의 지연 후 삽입

  function updateEvent(arg) {
    let event = {
      title: arg.event.title,
      start1: arg.event._instance.range.start,
      end: arg.event._instance.range.end,
      allDay: true,
    };
    $.ajax({
      url: "/eventUpdate/" + arg.event.id,
      method: "PUT",
      contentType: "application/json",
      data: JSON.stringify(event),
    });
  }
});
