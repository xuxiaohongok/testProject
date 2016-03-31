package com.zhidian3g.nextad.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.zhidian3g.nextad.web.filter.beans.GZipHttpServletRequestWrapper;

public class GzipFilter implements Filter {
	FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("=====init=====");
		this.filterConfig = filterConfig;

	}

	public void destroy() {
		System.out.println("=====destroy=====");
		this.filterConfig = null;

	}

	public void doFilter(ServletRequest request, ServletResponse response,
	FilterChain chain) throws IOException, ServletException {
		if(isGzipSupported((HttpServletRequest)request)) {
			chain.doFilter(new GZipHttpServletRequestWrapper((HttpServletRequest) request), response);
		} else {
			System.out.println("=====没有压缩=====");
			chain.doFilter(request, response);
		}
		System.out.println("=====doFilter=====");
	}
	
	public static boolean isGzipSupported(HttpServletRequest request) {
		String encodings = request.getHeader("Accept-Encoding");
		return ((encodings != null) && (encodings.indexOf("gzip") != -1));
	}

}