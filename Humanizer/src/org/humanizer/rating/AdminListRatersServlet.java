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
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.humanizer.rating.objects.Items;
import org.humanizer.rating.objects.Project;
import org.humanizer.rating.objects.RatingResult;
import org.humanizer.rating.objects.Tasks;
import org.humanizer.rating.objects.TasksByRater;
import org.humanizer.rating.utils.HTTPClient;

import com.google.gson.Gson;

/**
 * @author sonhv
 *
 * Showing results for a keyword rating
 */
@SuppressWarnings("serial")
public class AdminListRatersServlet extends HttpServlet {
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
	  
	  String projectId = req.getParameter("project");
	  String taskId = req.getParameter("task");
	  String taskName = req.getParameter("task_name");
	  
	  
	  //1. Get project data
	  String sURL = "http://humanizer.iriscouch.com/projects/_design/api/_view/list_projects?startkey=%22" + projectId + "%22&endkey=%22" + projectId + "%22";
	  String sResult = HTTPClient.request(sURL);
	  Project prj = new Project();
	  prj.initItemList(sResult);  
	  
	  List currentProject = (List)prj.getData().get(0);
	  
	  //2. Get tasks data
	  sURL = "http://humanizer.iriscouch.com/tasks/_design/api/_view/task_data?startkey=%22" + taskId + "%22&endkey=%22" + taskId + "%22";	  
	  sResult = HTTPClient.request(sURL);
	  Tasks task = new Tasks();
	  task.initTasksList(sResult);  	
	  
	  List currentTask = (List)task.getData().get(0);
	  
	  List raterData = (ArrayList)currentTask.get(currentTask.size() - 1);
	  
	
	    
	   req.setAttribute("project",currentProject);
	   req.setAttribute("task",currentTask);
	   req.setAttribute("raters",raterData);
	   req.setAttribute("task",taskId);
	   req.setAttribute("task_name",taskName);
	  //req.setAttribute("task_status", rater.getStatus());
	  
	  RequestDispatcher dispatcher = req.getRequestDispatcher("/admin_list_raters.jsp");

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
