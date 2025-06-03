package kr.co.semi.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/* Intercepter : 로그인을 안했을 때 모든 요청 가로채서 접근 막기
 * by 김성원
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

	    @Override
	    public boolean preHandle(HttpServletRequest request,
	                             HttpServletResponse response,
	                             Object handler) throws Exception {

	        HttpSession session = request.getSession(false); // 세션이 없으면 null 반환

	        // 로그인 상태 확인
	        Object loginMember = (session != null) ? session.getAttribute("loginMember") : null;
	        
	        if (loginMember == null) {
	            if (session == null) {
	                session = request.getSession(); // 세션이 없으면 생성
	            }
	            // 비로그인 시 메시지 출력
	            session.setAttribute("message", "로그인 후 이용해주세요.");
	            // 비로그인 상태 → 메인 페이지로 리디렉트
	            response.sendRedirect("/");
	            return false; // 컨트롤러 접근 차단
	        }

	        return true; // 로그인 상태 → 컨트롤러 실행 허용
	    }
	}
	
