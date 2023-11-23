package com.damoyeo.healthyLife.filter;



import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FirstFilter implements Filter{
	private List<String> publicPaths;
	
	 @Override
	    public void init(FilterConfig filterConfig) throws ServletException {
	        log.info("FirstFilter가 생성 됩니다.");
	    }

	    @Override
	    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
	        log.info("==========First 필터 시작!==========");
	        filterChain.doFilter(servletRequest, servletResponse);
	        log.info("==========First 필터 종료!==========");
	    }

	    @Override
	    public void destroy() {
	        log.info("FirstFilter가 사라집니다.");
	    }
}
