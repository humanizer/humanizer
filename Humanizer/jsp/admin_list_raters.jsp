<%@ page import="java.io.*" %> 
<%@page language="java" import="java.util.*" %>
<head>
  <meta http-equiv="Content-Type" content="text/html" charset="UTF-8" />
  <link href="css/style.css" rel="stylesheet" type="text/css" />
  <title>Keyword listing</title>
  
</head>

<body>
<div class="container">
  
  <!--<div class="row" id="topbar">
    
  </div> -->
  <div class="keyword">Rater details for task <b><%=request.getAttribute("task_name") %></b>: </div>
  <div class="list-key">

	<table>
		<tr>
		<th class="for_table">No</th>
		<th class="for_table">User Name</th>
		<th class="for_table">Verified</th>
		<th class="for_table">Joined date</th>
		<th class="for_table">Edit</th>
		</tr>
		<%
			List raters= (List)request.getAttribute("raters");
			Iterator iRaters;
			int index = 1;
			for (iRaters=raters.iterator(); iRaters.hasNext();) {	
				List raterData= (List)iRaters.next();
				Iterator iRater;
				for (iRater=raterData.iterator(); iRater.hasNext();) {
					String raterId = (String) iRater.next();		
					String raterRev = (String) iRater.next();						
					String raterEmail = (String) iRater.next();			
					String raterVerified = (String) iRater.next();											
					String join_date = "";

				

				%>
							<tr>
		<td class="for_table"><%=index %></td>
		<td class="for_table"><%=raterEmail %></td>
		<td class="for_table"><%=raterVerified %></td>
		<td class="for_table"><%=join_date %></td>
		<td class="for_table"><a href="">Delete</a></td>
			</tr>
				<%
				index += 1;								
				break;
				
				}
			}

		%>
		
		<!--th class="for_table">Rating Details</th-->
		
 
    </table> 

    
  </div>
  <%
	List project = (List)request.getAttribute("project");
	Iterator iProject;
	int maxRater = raters.size();
	for (iProject=project.iterator(); iProject.hasNext();) {	
		String prjId = (String) iProject.next();
		String prjRev = (String) iProject.next();
		String prjName = (String) iProject.next();
		String prjMaxRater = (String) iProject.next();
		maxRater = Integer.parseInt(prjMaxRater);
		break;
	}
	int remainRater = maxRater - raters.size();
	if (remainRater < 0){
		remainRater = 0;
	}
  %>
  <form id="submit" name="submit" method="post" action="admin_list_raters">
  <div>This task could have up to <%=remainRater %> raters. </div>
  <%
	if (remainRater > 0) {
  %>
		<div> Avaialabe raters: <select name="rater"><%
			List availRaters = (List)request.getAttribute("remainRaters");
			Iterator iAvailRaters;
			
			for (iAvailRaters=availRaters.iterator(); iAvailRaters.hasNext();) {	
				List availRater = (List) iAvailRaters.next();
				Iterator iAvailRater;			
				for (iAvailRater=availRater.iterator(); iAvailRater.hasNext();) {	
					String raterId = (String) iAvailRater.next();		
					String raterRev = (String) iAvailRater.next();						
					String raterEmail = (String) iAvailRater.next();			
					String raterVerified = (String) iAvailRater.next();						
					String join_date = "";
					String desc = raterEmail;
					%>
					 <option value="<%=raterEmail %>"><%=raterEmail %></option>
					<%
					break;
				}
			}
		
		%></select></div> 
		<input type="hidden" name="project" id="project" value="<%=request.getAttribute("projectId")%>" />
		<input type="hidden" name="task" id="task" value="<%=request.getAttribute("taskId")%>" />
		<input type="hidden" name="task_name" id="task_name" value="<%=request.getAttribute("task_name")%>" />
		<input type="hidden" name="action" id="action" value="add" />
		<input type="submit" name="submit" id="submit" />
		
  <%
	}
  %>
  <form>
  <div class="pagination">
    <ul class="pages">
      <li><a href="#">First</a></li>
      <li class="prev"><a href="#">Previous</a></li>
      <li><a class="active" href="#">1</a></li>
      <li><a href="#">2</a></li>
      <li><a href="#">3</a></li>
      <li><a href="#">4</a></li>
      <li><a href="#">5</a></li>
      <li class="next"><a href="#">Next</a></li>
      <li><a href="#">Last</a></li>
    </ul>
  </div>
  <div class="sb_foot clearfix">
    <ul class="sw_footL clearfix">
      <li><span>&copy; 2013 Company</span> | </li>
      <li><a href="#">Privacy and Cookies</a> | </li>
      <li><a href="#">Legal</a> | </li>
      <li><a target="_blank" id="sb_help" href="#">Help</a> | </li>
      <li><a h="ID=FD,65.1" id="sb_feedback" href="#">Feedback</a></li>
	  <li><a href="/logout" id="sb_feedback" href="#">Logout</a></li>
    </ul>
    <div class="clear"></div>
  </div>
</div>
<!--div class="rating-bottom">
  <div class="bottomBody">
    <ul class='star-rating'>
      <li class='current-rating' style='width:76px;'> Currently 3.5/5 Stars.</li>
      <li><a href='#' title='1 star out of 5' class='one-star'>1</a></li>
      <li><a href='#' title='2 stars out of 5' class='two-stars'>2</a></li>
      <li><a href='#' title='3 stars out of 5' class='three-stars'>3</a></li>
      <li><a href='#' title='4 stars out of 5' class='four-stars'>4</a></li>
      <li><a href='#' title='5 stars out of 5' class='five-stars'>5</a></li>
    </ul>
    <input type="submit" class="btnSubmit" value=" "/>
  </div>
</div-->


</body>
</html>
