package com.zhidian3g.nextad.web.filter.beans;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import com.zhidian3g.common.utils.GZIP;

public class GZipHttpServletRequestWrapper extends HttpServletRequestWrapper {
	public GZipHttpServletRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	public String[] getParameterValues(String parameter) {
		System.out.println("name===" + parameter);
		String[] values = super.getParameterValues(parameter);
		System.out.println("Arrays.toString=" + Arrays.toString(values));
		if (values == null) {
			return null;
		}

		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = cleanXSS(values[i]);
		}
		return encodedValues;
	}

	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		return cleanXSS(value);

	}


	private String cleanXSS(String value) {
		System.out.println("value=" + value);
		try {
			value = GZIP.unCompress(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

}