/**
 * 
 */
package org.humanizer.rating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.datanucleus.store.types.sco.backed.List;
import org.humanizer.rating.objects.Items;
import org.humanizer.rating.objects.Project;
import org.humanizer.rating.objects.TasksByRater;
import org.humanizer.rating.utils.HTTPClient;

import com.google.gson.Gson;

/**
 * @author sonhv
 *
 * Showing results for a keyword rating
 */
@SuppressWarnings("serial")
public class AdminListProjectsServlet extends HttpServlet {
  //private static final Logger log = Logger.getLogger(AuthenServlet.class.getName());
  /**
   * @author sonhv
   * 
   * GET handling
   * Redirect to POST 
   */  
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
     throws IOException {
    doPost(req, resp);
  }
  
  /**
   * @author sonhv
   * 
   * POST handling
   * Listing details for rate 
   */
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
  HttpSession sess = req.getSession(true);
  String username = (String) sess.getAttribute("adminuser");	
	if (username == null){
		resp.sendRedirect("/admin_login.jsp");
		return;
	}  
  
  //perform get rate list by keyword and task
  StringBuilder sb = new StringBuilder();
  
  //1. Get projects list
  String sURL = "http://humanizer.iriscouch.com/projects/_design/api/_view/list_projects";
  String sResult = HTTPClient.request(sURL);
  Project prj = new Project();
  prj.initItemList(sResult);  
  
    
   req.setAttribute("data",prj.getData());
  //req.setAttribute("task_status", rater.getStatus());
  
  RequestDispatcher dispatcher = req.getRequestDispatcher("/admin_list_projects.jsp");

  if (dispatcher != null){
    try {
      dispatcher.forward(req, resp);
    } catch (ServletException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  } 

  }  
}
