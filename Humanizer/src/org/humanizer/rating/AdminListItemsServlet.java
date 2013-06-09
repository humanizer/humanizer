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
import org.humanizer.rating.utils.PaginationHelper;

import com.google.gson.Gson;

/**
 * @author sonhv
 * 
 *         Showing results for a keyword rating
 */
@SuppressWarnings("serial")
public class AdminListItemsServlet extends HttpServlet {
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
		String rater_count = req.getParameter("raters");

		// 1. Get tasks data
		String sURL = "http://humanizer.iriscouch.com/tasks/_design/api/_view/task_data?startkey=%22"
				+ taskId + "%22&endkey=%22" + taskId + "%22";
		String sResult = HTTPClient.request(sURL);
		Tasks prj = new Tasks();
		prj.initTasksList(sResult);

		// 2. Get items list from this task
		sURL = "http://humanizer.iriscouch.com/items/_design/index/_view/items_in_task?startkey=%22"
				+ taskId + "%22&endkey=%22" + taskId + "%22&include_docs=true";
		PaginationHelper paginationHelper = new PaginationHelper(sURL, req) {

			@Override
			public int count() {
				String sResult = HTTPClient.request(getsURL());
				Items item = new Items();
				item.initItemListForTask(sResult);
				return item.getData().size();
			}
		};
		sURL = paginationHelper.buildURL();
		sResult = HTTPClient.request(sURL);
		Items item = new Items();
		item.initItemListForTask(sResult);

		List data = (List) item.getData();
		List newData = new ArrayList();
		List currentTask = (ArrayList) prj.getData().get(0);

		List raterData = (ArrayList) currentTask.get(currentTask.size() - 1);

		for (int i = 0; i < data.size(); i++) {
			List tmp = (List) data.get(i);
			String itemId = (String) tmp.get(0);
			sURL = "http://humanizer.iriscouch.com/ratings/_design/api/_view/rating_by_item_task?startkey=%22"
					+ itemId
					+ "%7C"
					+ taskId
					+ "%22&endkey=%22"
					+ itemId
					+ "%7C" + taskId + "%22&include_docs=true";
			sResult = HTTPClient.request(sURL);
			RatingResult res = new RatingResult();

			List ret = (List) res.init(sResult, raterData);

			tmp.add(ret);
			newData.add(tmp);
		}

		req.setAttribute("data", newData);
		req.setAttribute("raters", raterData);
		req.setAttribute("task", taskId);
		req.setAttribute("task_name", taskName);
		// req.setAttribute("task_status", rater.getStatus());

		RequestDispatcher dispatcher = req
				.getRequestDispatcher("/admin_list_items.jsp");

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
