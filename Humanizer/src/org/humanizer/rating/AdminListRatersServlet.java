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
import org.humanizer.rating.objects.Rater;
import org.humanizer.rating.objects.RatingResult;
import org.humanizer.rating.objects.Tasks;
import org.humanizer.rating.objects.TasksByRater;
import org.humanizer.rating.utils.HTTPClient;
import org.humanizer.rating.utils.PaginationHelper;

import com.google.gson.Gson;

/**
 * @author sonhv
 * 
 *         Showing results for a keyword rating
 */
@SuppressWarnings("serial")
public class AdminListRatersServlet extends HttpServlet {
	// private static final Logger log =
	// Logger.getLogger(AuthenServlet.class.getName());
	/**
	 * @author sonhv
	 * 
	 *         GET handling Redirect to POST
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	/**
	 * @author sonhv
	 * 
	 *         POST handling Listing details for rate
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession sess = req.getSession(true);
		String username = (String) sess.getAttribute("adminuser");
		if (username == null) {
			resp.sendRedirect("/admin_login.jsp");
			return;
		}

		String projectId = req.getParameter("project");
		String taskId = req.getParameter("task");
		String taskName = req.getParameter("task_name");

		// 1. Get project data
		String sURL = "http://humanizer.iriscouch.com/projects/_design/api/_view/list_projects?startkey=%22"
				+ projectId + "%22&endkey=%22" + projectId + "%22";
		String sResult = HTTPClient.request(sURL);
		Project prj = new Project();
		prj.initItemList(sResult);

		List currentProject = (List) prj.getData().get(0);

		// 2. Get tasks data
		sURL = "http://humanizer.iriscouch.com/tasks/_design/api/_view/task_data?startkey=%22"
				+ taskId + "%22&endkey=%22" + taskId + "%22";
		sResult = HTTPClient.request(sURL);
		Tasks task = new Tasks();
		task.initTasksList(sResult);

		List currentTask = (List) task.getData().get(0);

		List raterData = (ArrayList) currentTask.get(currentTask.size() - 1);

		// 3. Get raters data
		sURL = "http://humanizer.iriscouch.com/raters/_design/api_raters/_view/raters";

		PaginationHelper paginationHelper = new PaginationHelper(sURL, req) {

			@Override
			public int count() {
				String sURL = "http://humanizer.iriscouch.com/tasks/_design/api/_view/task_data?startkey=%22"
						+ getHttpServletRequest().getParameter("task") + "%22&endkey=%22" + getHttpServletRequest().getParameter("task") + "%22";
				String sResult = HTTPClient.request(sURL);
				Tasks task = new Tasks();
				task.initTasksList(sResult);

				List currentTask = (List) task.getData().get(0);

				List raterData = (ArrayList) currentTask
						.get(currentTask.size() - 1);

				sResult = HTTPClient.request(getsURL());
				Rater raters = new Rater();
				List lstRater = raters.initData(sResult, raterData);

				List remainRaterData = new ArrayList();

				for (int i = 0; i < lstRater.size(); i++) {
					List tmp = (List) lstRater.get(i);
					String flag = (String) tmp.get(tmp.size() - 1);
					if (flag.equals("1")) {
						// available list
						remainRaterData.add(tmp);
					} else {
						// current list
						raterData.add(tmp);
					}

				}
				return remainRaterData.size();
			}
		};
		sURL = paginationHelper.buildURL();

		sResult = HTTPClient.request(sURL);
		Rater raters = new Rater();
		List lstRater = raters.initData(sResult, raterData);

		raterData.clear();
		List remainRaterData = new ArrayList();

		for (int i = 0; i < lstRater.size(); i++) {
			List tmp = (List) lstRater.get(i);
			String flag = (String) tmp.get(tmp.size() - 1);
			if (flag.equals("1")) {
				// available list
				remainRaterData.add(tmp);
			} else {
				// current list
				raterData.add(tmp);
			}

		}

		req.setAttribute("projectId", projectId);
		req.setAttribute("taskId", taskId);
		req.setAttribute("task_name", taskName);

		req.setAttribute("project", currentProject);
		req.setAttribute("task", currentTask);
		req.setAttribute("raters", raterData);
		req.setAttribute("remainRaters", remainRaterData);

		// req.setAttribute("task_status", rater.getStatus());

		RequestDispatcher dispatcher = req
				.getRequestDispatcher("/admin_list_raters.jsp");

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
