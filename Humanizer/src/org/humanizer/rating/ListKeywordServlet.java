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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.humanizer.rating.objects.Items;
import org.humanizer.rating.objects.TasksByRater;
import org.humanizer.rating.utils.HTTPClient;

import com.google.gson.Gson;

/**
 * @author sonhv
 *
 * Listing keyword rating (tasks) for user
 */
public class ListKeywordServlet extends HttpServlet {
  //private static final Logger log = Logger.getLogger(AuthenServlet.class.getName());
  
  /**
   * @author sonhv
   * 
   * GET handling. Forward to POST
   * 
   */  
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    doPost(req, resp);
  }
  
  /**
   * @author sonhv
   * 
   * POST handling
   * Using username, request task lists from couchDB 
   */
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    
    HttpSession sess = req.getSession(true);
    String username = (String) sess.getAttribute("username");	
	if (username == null){
		resp.sendRedirect("/login.jsp");
		return;
	}
    StringBuilder sb = new StringBuilder();
    
    //1. Get items list
    String sURL = "http://humanizer.iriscouch.com/items/_design/api_items/_view/items_list";
    String sResult = HTTPClient.request(sURL);
    Items item = new Items();
    item.initItemList(sResult);
    
    
    //2. Get Task list for current user
    sURL = "http://humanizer.iriscouch.com/tasks/_design/api/_view/rater_tasks_with_items?startkey=%22" + username + "%22&endkey=%22" + username + "%22";
    sResult = HTTPClient.request(sURL);
    
    
    TasksByRater rater = new TasksByRater();
    rater.setItemList(item.getItemList());
    rater.init(sResult);
   
    req.setAttribute("data", rater.getData());
    
    

    RequestDispatcher dispatcher = req.getRequestDispatcher("/list_keyword.jsp");

    if (dispatcher != null){
      try {
        dispatcher.forward(req, resp);
      } catch (ServletException e) {
        e.printStackTrace();
      }
    } 
  }  

}

