package org.humanizer.rating.utils;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.humanizer.rating.objects.Items;
import org.humanizer.rating.objects.TasksByRater;

public abstract class PaginationHelper {

	private static final int NUMBER_PAGES_SHOW = 5;
	private static final String TOTAL_PAGES_ATTRIBUTE = "totalPages";
	private static final String TOTAL_ROWS_ATTRIBUTE = "totalRows";
	private String sURL;
	private HttpServletRequest httpServletRequest;

	public PaginationHelper(String sURL, HttpServletRequest httpServletRequest) {
		this.sURL = sURL;
		this.httpServletRequest = httpServletRequest;
		initAttribute();
	}

	public String getsURL() {
		return sURL;
	}
	
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public abstract int count();

	public void initAttribute() {
		int rows = count();

		int numberPages = rows / NUMBER_PAGES_SHOW;
		if (rows % NUMBER_PAGES_SHOW != 0) {
			numberPages++;
		}
		httpServletRequest.setAttribute(TOTAL_PAGES_ATTRIBUTE, numberPages);
		httpServletRequest.setAttribute(TOTAL_ROWS_ATTRIBUTE, rows);
	}

	private StringBuffer requestFirstListener() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(this.sURL);
		stringBuffer.append(getSpritURL());
		stringBuffer.append("limit=" + NUMBER_PAGES_SHOW);

		return stringBuffer;
	}

	public String buildURL() {
		StringBuffer stringBuffer = requestFirstListener();
		stringBuffer = requestListener(stringBuffer);
		
		return stringBuffer.toString();
	}
	
	private String getSpritURL(){
		if (this.sURL.contains("?")) {
			return "&";
		} 
		return "?";
	}

	private StringBuffer requestListener(StringBuffer stringBuffer) {
		if ((httpServletRequest.getParameter("limit") != null)
				&& (httpServletRequest.getParameter("skip") != null)) {

			String limit = httpServletRequest.getParameter("limit").toString();
			String skip = httpServletRequest.getParameter("skip").toString();

			stringBuffer = new StringBuffer();
			stringBuffer.append(this.sURL);
			stringBuffer.append(getSpritURL());
			
			stringBuffer.append("limit=" + limit + "&skip=" + skip);
		}
		return stringBuffer;
	}

}
