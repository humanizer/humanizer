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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.humanizer.rating.objects.Rater;
import org.humanizer.rating.objects.User;
import org.humanizer.rating.utils.HTTPClient;



/**
 * @author sonhv
 * 
 * Servlet for user authentication 
 */
@SuppressWarnings("serial")
public class AdminServlet extends HttpServlet {
  //private static final Logger log = Logger.getLogger(AuthenServlet.class.getName());
  /**
   * @author sonhv
   * 
   * GET handling. Forward to POST
   *  
   */
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    doPost(req,resp);
  }

  /**
   * @author sonhv
   * 
   * POST handling
   * Accept user/pass from client, submit to couchDB and starting to authen 
   */
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
      
  HttpSession sess = req.getSession(true);
	  
    
    //1. Accept user/pass from client
    String user = req.getParameter("login-user");
    String password = req.getParameter("login-password");
    if ((user == null)||(password == null)){
    	resp.sendRedirect("/admin_login.jsp");
    	return;
    }
    
    String password2 = password;

    //2. MD5 pass for client
    try {
      java.security.MessageDigest md =
          java.security.MessageDigest.getInstance("MD5");
      byte[] array = md.digest(password.getBytes());
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
      }
      password2 = sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
      e.printStackTrace();
    }    
    
    //3. Authentication for admin to couch DB
    String sURL = "http://humanizer.iriscouch.com/users/_design/api/_view/list_user?startkey=%22" + user + "%7C" + password2 + "%22&endkey=%22" + user + "%7C" + password2  + "%22";
    String sResult = HTTPClient.request(sURL);    
   
   
      
    //4. Json string process
    try{
        User rater = new User();
        rater.init(sResult);        
        if (rater.getUsername() == null) {
            resp.sendRedirect("/admin_login.jsp");
	      } 
	      if ((rater.getUsername().equals(user)) &&
	          (rater.getPassword().equals(password2))) {
	        sess.setAttribute("adminuser", user);
	        resp.sendRedirect("/admin_list_projects");
	      } else {
	        resp.sendRedirect("/admin_login.jsp");
	      }    	
    }catch (Exception ex){
    	ex.printStackTrace();
    	resp.sendRedirect("/admin_login.jsp");
    	return;    	
    		
    }

    //5. check if rater is authenticated
  
  }  
}

