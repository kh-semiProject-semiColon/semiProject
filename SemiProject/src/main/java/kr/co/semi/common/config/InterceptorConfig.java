package kr.co.semi.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.semi.common.interceptor.LoginInterceptor;

// 인터셉터가 어떤 요청을 가로챌지 설정하는 클래스

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	
	
	 @Override
	    public void addInterceptors(InterceptorRegistry registry) {

	        registry.addInterceptor(new LoginInterceptor())
	            .addPathPatterns("/board/**", "/study/**", "/studyBoard/**") // 로그인 필요 경로
	            .excludePathPatterns("/login", "/signup", "/resources/**", "/css/**", "/js/**","/member/**"); // 허용 경로
	    }
	
}