package com.paraframework.common;



import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class CustomMultipartResolver extends CommonsMultipartResolver {
	public final static int MaxUploadSize = 10;
	
	public CustomMultipartResolver() {
		// TODO Auto-generated constructor stub
		 setMaxUploadSize(MaxUploadSize * 1024 * 1024);
		 setMaxInMemorySize(MaxUploadSize * 1024 * 1024);
		 setDefaultEncoding("UTF-8");
	}
}
