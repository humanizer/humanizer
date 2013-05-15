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

import java.util.List;
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
public class AdminListRatingsServlet extends HttpServlet {
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
	  String itemId = req.getParameter("item");
	  String title = req.getParameter("title");
	  
	  //1. Get tasks list from this project
	  String sURL = "http://humanizer.iriscouch.com/tasks/_design/api/_view/tasks_by_project?startkey=%22" + projectId + "%22&endkey=%22" + projectId + "%22";	  
	  String sResult = HTTPClient.request(sURL);
	  Tasks prj = new Tasks();
	  prj.initTasksList(sResult);  		  
	  List raterData = (ArrayList) prj.getData().get(prj.getData().size() - 1);
	  
	  //2. Get rating list from this task
	  sURL = "http://humanizer.iriscouch.com/ratings/_design/api/_view/rating_by_item_task?startkey=%22" + itemId + "%7C" + taskId + "%22&endkey=%22" +itemId + "%7C" + taskId + "%22&include_docs=true";	  
	  sResult = HTTPClient.request(sURL);
	  RatingResult res = new RatingResult();
	  List ret = (List) res.init(sResult, raterData);  
	  
	    
	   req.setAttribute("data",ret);
	   req.setAttribute("task",taskId);
	   req.setAttribute("title",title);
	   //req.setAttribute("task_name",taskName);
	  //req.setAttribute("task_status", rater.getStatus());
	  
	  RequestDispatcher dispatcher = req.getRequestDispatcher("/admin_list_ratings.jsp");

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
