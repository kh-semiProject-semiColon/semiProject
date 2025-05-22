$(document).ready(function () {
  let calendarTag = $("#calendar");
  let calendar = new FullCalendar.Calendar(calendarTag[0], {
    height: "550px",
    expandRows: true,
    slotMinTime: "00:00",
    slotMaxTime: "23:59",
    customButtons: {
      testButton: {
        text: "테스트버튼",
      },
    },
    headerToolbar: {
      left: "prevYear,prev,next,nextYear today",
      center: "title",
      right: "dayGridMonth,timeGridWeek,timeGridDay,listWeek",
    },
    initialView: "dayGridMonth",
    navLinks: true,
    editable: true,
    selectable: true,
    nowIndicator: true,
    dayMaxEvents: true,
    locale: "ko",

    events: "/study/calendarList",

    eventAdd: function (obj) {
      console.log("eventAdd : ", obj);
    },
    eventChange: function (obj) {
      console.log("eventChange : ", obj);
    },
    eventRemove: function (obj) {
      console.log("eventRemove : ", obj);
    },

    select: function (arg) {
      let title = prompt("일정 입력");
      if (title) {
        let newData = {
          title: title,
          start: arg.start,
          end: arg.end,
          allDay: arg.allDay,
        };

        $.ajax({
          url: "/study/calendarSave",
          method: "POST",
          dataType: "json",
          data: JSON.stringify(newData),
          contentType: "application/json",
          success: function (data) {
            if (data != null) {
              calendar.addEvent({
                id: data.calendarNo,
                title: data.title,
                start: data.start1,
                end: data.end,
                editable: true,
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
          url: "/study/calendarDelete",
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

    // 수정된 부분: 올바른 방식으로 이벤트 불러오기
    events: "/study/calendarList",
  });
  calendar.render();

  function updateEvent(arg) {
    let event = {
      title: arg.event.title,
      start1: arg.event._instance.range.start,
      end: arg.event._instance.range.end ? arg.event._instance.range.end : null,
      allDay: arg.event.allDay,
    };
    $.ajax({
      url: "/study/eventUpdate/" + arg.event.id,
      method: "PUT",
      contentType: "application/json",
      data: JSON.stringify(event),
    });
  }
});

const cal = document.querySelector("#calendar");
cal = () => {
  fetch("/study/calendarList")
    .then((resp) => resp.json())
    .then((result) => {});
};
