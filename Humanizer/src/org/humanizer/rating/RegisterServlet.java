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

import org.humanizer.rating.objects.Rater;

import com.google.gson.Gson;


/**
 * @author sonhv
 * 
 * Servlet for user authentication 
 */
@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
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
    String username = (String) req.getParameter("username");
    String password1 = (String) req.getParameter("password1");
    String password2 = (String) req.getParameter("password2");
    
    boolean bIsError = false;
    //validation
    if ((username == null) || (password1 == null) || (password2 == null)){
    	req.setAttribute("error", "Please enter username/password");
    	bIsError = true;
    }else{
    	if (!password1.equals(password2)){
	    	req.setAttribute("error", "Please enter the same value for password field");
	    	bIsError = true;
	    }
    }
    
  
  //MD5 pass for client
    if (!bIsError){
	    try {
	      java.security.MessageDigest md =
	          java.security.MessageDigest.getInstance("MD5");
	      byte[] array = md.digest(password1.getBytes());
	      StringBuffer sb = new StringBuffer();
	      for (int i = 0; i < array.length; ++i) {
	        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	      }
	      password1 = sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	      e.printStackTrace();
	    }        
    }
    Rater rater = new Rater();
    rater.email = username;
    rater.password = password1;
    rater.verified = false;
    
    Gson json = new Gson();
    String json_message = json.toJson(rater);
    
    //register
    if (!bIsError){
    	String message = json_message;
    	//Do Register 
	    try {
	      URL url = new URL("http://humanizer.iriscouch.com/raters/");
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

    }
    

    
    RequestDispatcher dispatcher = null;
    if (!bIsError){
    	dispatcher = req.getRequestDispatcher("/login.jsp");
    	req.setAttribute("username",username);
    }else{
    	dispatcher = req.getRequestDispatcher("/register.jsp");
    }
    

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

