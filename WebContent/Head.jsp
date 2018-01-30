<script type="text/javascript">
	function searchActor(){
		document.getElementById("mode").value="actor";
		document.getElementById("searchform").submit();
	}
	
	function searchMovie(){
		document.getElementById("mode").value="movie";
		document.getElementById("searchform").submit();
	}
	
	function SubmitForm(){
		var modenow = document.getElementById('modenow');
		if(modenow.innerText == "Actor"){
			document.getElementById("mode").value = "actor";
		}
		else{
			document.getElementById("mode").value = "movie";
		}
		document.getElementById("searchform").submit();
	}
</script>
<div style="padding:20px">
	<div name="logo">
		<a href="index.html"><img src="img/logo.png" style="width:125px; height:60px; float:left"/></a>
	</div>
	<div name="searchbar" style="padding-top:20px; margin-left:130px">
		<form action="Search?" id="searchform" method="get" onsubmit="SubmitForm()">
			<%
					out.print("<input type='text' id='query' name='query' style='width:500px; height:25px; font-size:20px' value='"+ request.getSession().getAttribute("Query")+"' />");		
			%>
			<input type="submit" id="search" value="" style="height:25px; width:25px; background:url(img/searchbutton.png) no-repeat;border:none;" />
			<input type="hidden" name="mode" id="mode" value="actor" />
		</form>
	</div>

	<div name="tabbar" style="margin-top:30px; height:28px;border-bottom-style:solid; border-bottom-width:2px; border-bottom-color:#c6c6c8; font-family:'Arial'">
		<%
			String mode = request.getSession().getAttribute("Mode").toString();
			if(mode.equals("actor") || mode==null || mode.equals("")){
				out.print("<div style='margin-left:130px;float:left;border-bottom-style:solid;border-bottom-width:2px; padding-bottom:10px'><a id='modenow' style='color:#000000; text-decoration:none' href='javascript:searchActor()'>Actor</a></div>");
				out.print("<div style='margin-left:200px; padding-bottom:10px'><a style='color:#c6c6c8; text-decoration:none' href='javascript:searchMovie()'>Movie</a></div>");
			}else{
				out.print("<div style='margin-left:130px;float:left; padding-bottom:10px'><a style='color:#c6c6c8; text-decoration:none' href='javascript:searchActor()'>Actor</a></div>");
				out.print("<div style='margin-left:20px;float:left; padding-bottom:10px;border-bottom-style:solid;border-bottom-width:2px;'><a id='modenow' style='color:#000000; text-decoration:none' href='javascript:searchMovie()'>Movie</a></div>");
			}
		
		%>
	</div>
</div>

