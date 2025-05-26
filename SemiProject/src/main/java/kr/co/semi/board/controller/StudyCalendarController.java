package kr.co.semi.board.controller;

import java.time.Instant;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import kr.co.semi.board.model.dto.Calendar;
import kr.co.semi.board.model.service.StudyCalendarService;
import kr.co.semi.member.model.dto.Member;

@SessionAttributes("loginMember")
@RestController
@RequestMapping("study")
public class StudyCalendarController {
	
	private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");
	private static final DateTimeFormatter KST_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private StudyCalendarService service;
	
	
	/**
     * 캘린더 일정 조회
     */
    @GetMapping("/calendarList")
    public List<Map<String, Object>> calendarList(@SessionAttribute("loginMember") Member loginMember) throws Exception {
    	
    	
    	return service.StudyCalendarList(loginMember).stream().map(cal -> {
            Map<String, Object> event = new HashMap<>();
            event.put("id", cal.getCalendarNo()); // 일정 ID
            event.put("title", cal.getTitle()); // 일정 제목
            event.put("start", cal.getStart1()); // 시작 날짜 (ISO 8601 형식)
            event.put("end", cal.getEnd()); // 종료 날짜 (ISO 8601 형식)
            event.put("color", cal.getColor()); // 일정 색깔
            return event;
        }).collect(Collectors.toList());
    }
    
    @PostMapping("/calendarSave")
    public Calendar calendarSave(@RequestBody Map<String, Object> map,
        						@SessionAttribute(value = "loginMember", required = false) Member loginMember) throws Exception {
        
        if (loginMember == null) {
            throw new IllegalStateException("로그인이 필요합니다."); // 또는 테스트 시 더미 데이터 설정 가능
        }

        Calendar vo = new Calendar();
        vo.setTitle((String) map.get("title"));
        vo.setColor((String)map.get("color"));

        // start 날짜 파싱
        if (map.get("start") != null) {
            Instant startInstant = Instant.parse((String) map.get("start"));
            ZonedDateTime startKST = startInstant.atZone(ZoneId.of("UTC")).withZoneSameInstant(KST_ZONE);
            vo.setStart1(startKST.format(KST_FORMATTER));
        }

        // end 날짜 파싱
        if (map.get("end") != null) {
            Instant endInstant = Instant.parse((String) map.get("end"));
            ZonedDateTime endKST = endInstant.atZone(ZoneId.of("UTC")).withZoneSameInstant(KST_ZONE);
            vo.setEnd(endKST.format(KST_FORMATTER));
        }

        vo.setMemberNo(loginMember.getMemberNo());

        service.calendarSave(vo);
        return vo;
    }

    /**
     * 캘린더 일정 삭제
     */
    @DeleteMapping("/calendarDelete/{no}")
    public String calendarDelete(@PathVariable("no") long calendarNo) {
        try {
            service.calendarDelete(calendarNo);
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