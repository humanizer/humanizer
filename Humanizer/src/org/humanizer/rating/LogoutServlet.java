/**
 * 
 */
package org.humanizer.rating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.humanizer.rating.objects.Rater;


/**
 * @author sonhv
 * 
 * Servlet for user authentication 
 */
@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {
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
    sess.removeAttribute("username");
    resp.sendRedirect("/login.jsp");
    
  }  
}

