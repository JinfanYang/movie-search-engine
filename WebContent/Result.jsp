<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Movie Search</title>
</head>
<body>
	<%@include file="Head.jsp" %>
	
	<div style="margin-left:100px; margin-top:10px; padding:10px; width: 600px; font-family:'Arial';">
		<%
			String bestGuess = request.getSession().getAttribute("BestGuess").toString();
			if(!bestGuess.equals("")){
				out.print("<span>Did you mean : <a id='bestguess' name='bestguess' href='javascript:searchBestGuess()' style='font-style:oblique;'>"+bestGuess+"</a></span>");
			}
		%>
	</div>
	<div name="list" style="float:left; padding-left:100px; padding-right:20px;margin-top:10px; width:700px; font-family:'Arial'">
		<%
			out.print(request.getSession().getAttribute("ResponseBody"));
		%>
	</div>
	
	<div name="recommendation" style=" width:240px; float:right; padding-right:100px; padding-left:50px; margin-top:10px; float:right; border-left-style:solid; border-left-width:1px; border-left-color:#c6c6c8; font-family:'Arial'">
		<%
			out.print(request.getSession().getAttribute("Recommen"));
		%>
	</div>

</body>
</html>
<script type="text/javascript">
	function searchBestGuess(){
		var bestGuess = document.getElementById('bestguess');
		document.getElementById("query").value= bestGuess.innerText;
		document.getElementById("searchform").submit();
	}
</script>