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
  <div class="keyword">Details for project <b><%=request.getAttribute("project_name") %></b>: </div>
  <div class="list-key">

	<table>
		<tr>
		<th class="for_table">No</th>
		<th class="for_table">Task</th>
		<th class="for_table">Query</th>
		<th class="for_table">Engine</th>
		<th class="for_table">Items</th>
		</tr>
	
      <%Iterator itr;%>
      <% List data= (List)request.getAttribute("data");
	  int index = 0;
      for (itr=data.iterator(); itr.hasNext();) {
        List lst = (List) itr.next();
        Iterator itr2;
        for (itr2=lst.iterator(); itr2.hasNext();) {
		  index = index + 1;
          String _id = (String) itr2.next();
          String _rev = (String) itr2.next();
		  String title = (String) itr2.next();
		  String query = (String) itr2.next();
		  String engine = (String) itr2.next();
		  
      %>
		<tr>
		<td class="for_table"><%=index %></td>
		<td class="for_table"><%=title %></td>
		<td class="for_table"><%=query %></td>
		<td class="for_table"><%=engine %></td>
		<td class="for_table"><a href="/admin_list_items?task=<%=_id%>&task_name=<%=title%>">View details</a></td>
       </tr>
        <%
          break; 
        }%>    
      <%}%>    
    </table> 

    
  </div>
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
