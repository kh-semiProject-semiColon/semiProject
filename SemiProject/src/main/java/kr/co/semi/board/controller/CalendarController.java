package kr.co.semi.board.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.co.semi.board.model.dto.Calendar;
import kr.co.semi.board.model.service.CalendarService;
import kr.co.semi.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CalendarController {


	 private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");
	 private static final DateTimeFormatter KST_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private CalendarService service;

    /**
     * 캘린더 일정 조회
     */
    @GetMapping("/calendarList")
    public List<Map<String, Object>> calendarList() throws Exception {
    	return service.calendarList().stream().map(cal -> {
            Map<String, Object> event = new HashMap<>();
            event.put("id", cal.getCalendarNo()); // 일정 ID
            event.put("title", cal.getTitle()); // 일정 제목
            event.put("start", cal.getStart1()); // 시작 날짜 (ISO 8601 형식)
            event.put("end", cal.getEnd()); // 종료 날짜 (ISO 8601 형식)
            return event;
        }).collect(Collectors.toList());
    }

    /**
     * 캘린더 일정 추가
     */
    @PostMapping("/calendarSave")
    public Calendar calendarSave(@RequestBody Map<String, Object> map,
    						@SessionAttribute("loginMember") Member loginMember) throws Exception {
    	
        Calendar vo = new Calendar();
        vo.setTitle((String) map.get("title"));
        
        // UTC → KST 변환
        ZonedDateTime startUTC = ZonedDateTime.parse((String) map.get("start")).withZoneSameInstant(KST_ZONE);
        vo.setStart1(startUTC.format(KST_FORMATTER));

        if (map.get("end") != null) {
            ZonedDateTime endUTC = ZonedDateTime.parse((String) map.get("end")).withZoneSameInstant(KST_ZONE);
            vo.setEnd(endUTC.format(KST_FORMATTER));
        }

        vo.setMemberNo(loginMember.getMemberNo());
        service.calendarSave(vo);
        return vo;
    }

    /**
     * 캘린더 일정 삭제
     */
    @DeleteMapping("/calendarDelete/{no}")
    public String calendarDelete(@PathVariable String no) {
        try {
            service.calendarDelete(no);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 캘린더 일정 수정
     */
    @PutMapping("/eventUpdate/{no}")
    public String eventUpdate(@PathVariable String no, @RequestBody Map<String, Object> map) {
        try {
        	Calendar vo = new Calendar();
            vo.setCalendarNo(Long.valueOf(no));
            vo.setTitle((String) map.get("title"));

            if (map.get("start1") != null) {
                ZonedDateTime start = ZonedDateTime.parse(map.get("start1").toString()).withZoneSameInstant(KST_ZONE);
                vo.setStart1(start.format(KST_FORMATTER));
            }

            if (map.get("end") != null) {
                ZonedDateTime end = ZonedDateTime.parse(map.get("end").toString()).withZoneSameInstant(KST_ZONE);
                vo.setEnd(end.format(KST_FORMATTER));
            }

            service.eventUpdate(vo);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
}