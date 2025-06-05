package kr.co.semi.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.semi.common.interceptor.LoginInterceptor;
import kr.co.semi.common.interceptor.SessionUpdateInterceptor;

// 인터셉터가 어떤 요청을 가로챌지 설정하는 클래스

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	
	 @Autowired
	   private SessionUpdateInterceptor sessionUpdateInterceptor;
	 
	 @Autowired
	 private LoginInterceptor LoginInterceptor;
	   
	 @Override
	   public void addInterceptors(InterceptorRegistry registry) {

	        registry.addInterceptor(LoginInterceptor)
	            .addPathPatterns("/board/**", "/study/**", "/studyBoard/**", "/hire/**") // 로그인 필요 경로
	            .excludePathPatterns("/login", "/signup", "/resources/**", "/css/**", "/js/**","/member/**"); // 허용 경로
	        
	        registry.addInterceptor(sessionUpdateInterceptor)
            .addPathPatterns("/studyBoard/**"); // 게시판 요청에만 적용
	 }
	
}
