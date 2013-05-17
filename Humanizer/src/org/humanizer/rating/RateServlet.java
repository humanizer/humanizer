/**
 * 
 */
package org.humanizer.rating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.humanizer.rating.objects.Items;
import org.humanizer.rating.objects.RatingResult;
import org.humanizer.rating.utils.HTTPClient;

import com.google.gson.Gson;

/**
 * @author sonhv
 *  
 * Showing for handling rating/updating search result
 */
@SuppressWarnings("serial")
public class RateServlet extends HttpServlet {
  //private static final Logger log = Logger.getLogger(AuthenServlet.class.getName());
  
  /**
   * @author sonhv
   * GET handling
   * Redirect to POST
   */
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    doPost(req,resp);
  }
  
  /**
   * @author sonhv
   * POST handling
   * Perform rate on a search result
   */
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    HttpSession sess = req.getSession(true);
    String username = (String) sess.getAttribute("username");

	if (username == null){
		resp.sendRedirect("/login.jsp");
		return;
	}      
    String m_url = req.getParameter("url");
    String keyword = req.getParameter("query");
    String task = req.getParameter("task");
    String task_id = req.getParameter("task");
    String point = req.getParameter("rating");
    String note = req.getParameter("note");
    String type = req.getParameter("type");
    String item_id = req.getParameter("item_id");

    Gson json = new Gson();
    RatingResult rating = new RatingResult();
    //rating.task_id = task;
    //rating.url = m_url;
    rating.rater = username;
    rating.relevance = point;
    rating.note = note;
    rating.item_id = item_id;
    rating.task_id = task_id;
    rating.time_stamp = String.valueOf(System.currentTimeMillis() / 1000L);
    if (type.equals("change_rate")){
    	rating._id = req.getParameter("_id");
    	rating._rev = req.getParameter("_rev");
    	
    }    
    
    String json_message = json.toJson(rating);
    //String message = URLEncoder.encode(json_message, "UTF-8");
    String message = json_message;
    //1. Perform rate on a keyword 
    try {
      URL url = new URL("http://humanizer.iriscouch.com/ratings/");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
	  connection.setRequestProperty("Authorization",
	    		"Basic aHVtYW5pemVyOjEyMzQ1Ng==");      

      OutputStreamWriter writer =
          new OutputStreamWriter(connection.getOutputStream());
      writer.write(message);
      writer.close();
  
      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        // OK
      } else {
        // Server returned HTTP error code.
      }
    } catch (MalformedURLException e) {
      // ...
      e.printStackTrace();
    } catch (IOException e) {
      // ...
      e.printStackTrace();
    }
    
   
        
    
    //2. Request rating information, resubmit performed    
    StringBuilder sb = new StringBuilder();
    String sURL = "";
    String sResult = "";

    /*
    //2.1. Get items list    
    String sURL = "http://humanizer.iriscouch.com/items/_design/index/_view/items_list";
    String sResult = HTTPClient.request(sURL);
    Items item = new Items();
    item.initItemList(sResult);
    */  
    
    //2.1. Get Items list 	
    sURL = "http://humanizer.iriscouch.com/items/_design/index/_view/items_in_task?startkey=%22" + task + "%22&endkey=%22" + task + "%22&include_docs=true";
    sResult = HTTPClient.request(sURL);
    Items item = new Items();
    item.initItemListForTask(sResult);    
        
    
    
    //2.2. Get rating by rater list
    sURL = "http://humanizer.iriscouch.com/ratings/_design/api/_view/rating_by_rater_task_item?startkey=%22" + username + "%7C" + task  + "%7C" + item_id + "%22&endkey=%22" + username + "%7C" + task + "%7C" + item_id + "%22";
    sResult = HTTPClient.request(sURL);
    
    
    json = new Gson();
    RatingResult rater = new RatingResult();
    if (rater.init(sResult,item_id, username) == true) {
		
	
      req.setAttribute("relevance", rater.relevance);
      req.setAttribute("note", rater.note);
      req.setAttribute("rater", rater.rater);
      req.setAttribute("_rev", rater._rev);
      req.setAttribute("_id", rater._id);
    
    }else{
      req.setAttribute("relevance", "0");
      req.setAttribute("note", "");
    }
    //rater.init(sb.toString(),"\""+ keyword +"\"");
    
    String url = rater.getURL(item.getItemList());
    
    req.setAttribute("task", task);
    req.setAttribute("url", url);
    req.setAttribute("keyword", keyword);
    req.setAttribute("item_id", item_id);

    RequestDispatcher dispatcher = req.getRequestDispatcher("/rating.jsp");

    if (dispatcher != null) {
      try {
        dispatcher.forward(req, resp);
      } catch (ServletException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } 
  }  

}
