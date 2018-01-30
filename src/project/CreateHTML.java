package project;

import java.util.ArrayList;

public class CreateHTML {
	
	public static String createAHTML(ArrayList<Actor> actors){
		StringBuffer buf = new StringBuffer();
		int range = 5;
		if(actors.size() < 5){
			range = actors.size();
		}
		for(int i = 0; i< range; i++){
			if((i+1)%2 != 0){
				buf.append("<div style='padding-top:30px'>");
				
			}
			buf.append(createRecActor(actors.get(i), i+1));
			if((i+1)%2 == 0){
				buf.append("</div>");
			}
		}
		return buf.toString();
	}
	
	public static String createMHTML(ArrayList<Movie> movies){
		StringBuffer buf = new StringBuffer();
		int range = 5;
		if(movies.size() < 5){
			range = movies.size();
		}
		for(int i = 0; i< range; i++){
			if((i+1)%2 != 0){
				buf.append("<div style='padding-top:30px'>");
				
			}
			buf.append(createRecMovie(movies.get(i), i+1));
			if((i+1)%2 == 0){
				buf.append("</div>");
			}
		}
		return buf.toString();
	}
	
	
	private static String createRecActor(Actor actor, int i){
			StringBuffer buf = new StringBuffer();
			if(i%2 == 0){
				buf.append("<div style='margin-left:120px'>");
			}
			else{
				buf.append("<div style='float:left'>");
			}
			// Images
			if(actor.getWikiUrl() != null){
				buf.append("<a href="+actor.getWikiUrl()+"><img src='"+actor.getPictureUrl()+"' style='width:70px;height:90px;'></a>");
			}
			else{
				buf.append("<a href='#'><img src='"+actor.getPictureUrl()+"' style='width:70px;height:90px;'></a>");
			}
			
			buf.append("<br>");
			
			if(actor.getWikiUrl() != null){
				buf.append("<a href="+actor.getWikiUrl()+"><div style='font-size:12px; width:70px'>"+actor.getName()+"</div></a>");
			}
			else{
				buf.append("<span style='font-size:12px'>"+actor.getName()+"<span>");
			}
			
			buf.append("</div>");
			return buf.toString();
	}
	
	private static String createRecMovie(Movie movie, int i){
		StringBuffer buf = new StringBuffer();
		if(i%2 == 0){
			buf.append("<div style='margin-left:120px'>");
		}
		else{
			buf.append("<div style='float:left'>");
		}
		// Images
		if(movie.getWikiUrl() != null){
			buf.append("<a href="+movie.getWikiUrl()+"><img src='"+movie.getPictureUrl()+"' style='width:70px;height:90px;'></a>");
		}
		else{
			buf.append("<a href='#'><img src='"+movie.getPictureUrl()+"' style='width:70px;height:90px;'></a>");
		}
		
		buf.append("<br>");
		
		if(movie.getWikiUrl() != null){
			buf.append("<a href="+movie.getWikiUrl()+"><div style='font-size:12px; width: 70px'>"+movie.getName()+"</div></a>");
		}
		else{
			buf.append("<span style='font-size:12px'>"+movie.getName()+"<span>");
		}
		
		buf.append("</div>");
		return buf.toString();
	}
}
