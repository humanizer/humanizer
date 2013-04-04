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
 * Servlet for showing rating result for a search 
 */
@SuppressWarnings("serial")
public class RatingServlet extends HttpServlet {
  //private static final Logger log = Logger.getLogger(AuthenServlet.class.getName());
  
  /**
   * @author sonhv
   *
   * GET method
   * Redirect to POST 
   */
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    doPost(req,resp);
  }
  
  
  /**
   * @author sonhv
   *
   * POST method
   * Get task info
   */
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    HttpSession sess = req.getSession(true);
    String username = (String) sess.getAttribute("username");

	if (username == null){
		resp.sendRedirect("/login.jsp");
		return;
	}  
    
    String url_check = req.getParameter("url");
    String keyword = req.getParameter("keyword");
    String task = req.getParameter("task");
    String item_id = req.getParameter("item_id");
    
    //perform get task info by username
    StringBuilder sb = new StringBuilder();
    
    //1. Get items list    
    String sURL = "http://humanizer.iriscouch.com/items/_design/api_items/_view/items_list";
    String sResult = HTTPClient.request(sURL);
    Items item = new Items();
    item.initItemList(sResult);  
    
    
    //2. Get rating by rater list
    sURL = "http://humanizer.iriscouch.com/ratings/_design/api/_view/rating_by_rater?startkey=%22" + username + "," + item_id + "%22&endkey=%22" + username + "," + item_id + "%22";
    sResult = HTTPClient.request(sURL);
    
    
    Gson json = new Gson();
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
