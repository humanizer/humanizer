package org.humanizer.rating;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SideBySideServlet extends HttpServlet {
	private static final long serialVersionUID = 7206843565601528726L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		System.out.println("Got here.");
		RequestDispatcher dispatcher = req.getRequestDispatcher("/sxs.jsp");
	    resp.setCharacterEncoding("UTF-8");
	    resp.setContentType("text/html; charset=UTF-8");    

	    if (dispatcher != null){
	        dispatcher.forward(req, resp);
	    } 
	}
}
