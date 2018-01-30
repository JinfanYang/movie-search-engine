package project;

public class ScoredMovie implements Comparable<ScoredMovie> {
	private Movie _movie;
	private double _score;
	
	public ScoredMovie(Movie movie, double score){
		_movie = movie;
		_score = score;
	}
	
	public String asHtmlResult(){
		StringBuffer buf = new StringBuffer();
		buf.append("<div style='padding-top:30px'>");
		// Images
		String addr = _movie.getWikiUrl();
		if(addr == null){
			addr="#";
		}
		buf.append("<div><a href="+addr+"><img src='"+_movie.getPictureUrl()+"' style='width:70px;height:90px;float:left'></a></div>");		
		// Attributes
		buf.append("<div style='margin-left:100px; line-height:15px'>");
		if(addr.equals("#")){
			buf.append("<span style='font-weight:bold; font-size:12px'>").append(_movie.getName()).append("</span><br>");
		}
		else{
			buf.append("<a href="+addr+"><span style='font-weight:bold; font-size:12px'>").append(_movie.getName()).append("</a></span><br>");
		}
		buf.append("<span style='font-weight:bold; font-size:11px'>IMDB Rating:&nbsp;&nbsp;</span>");
		buf.append("<span style='font-weight:normal;font-size:11px'>").append(_movie.getRating()).append("</span>");
		buf.append("<span style='font-weight:normal;font-size:11px'>&nbsp;(").append(_movie.getRatingsCount()).append("&nbsp;&nbsp;views)</span><br>");
		buf.append("<span style='font-weight:bold;font-size:11px'>Director:&nbsp;&nbsp;<span>");
		buf.append("<span style='font-weight:normal;font-size:11px'>").append(_movie.getDirector()).append("</span><br>");
		buf.append("<span style='font-weight:bold;font-size:11px'>Genres:&nbsp;&nbsp;</span>");
		
		for(int i = 0; i<_movie.getGenres().size(); i++){
			buf.append("<span style='font-weight:normal;font-size:11px'>");
			buf.append(_movie.getGenres().get(i)).append("</span>");
			if(i < _movie.getGenres().size()-1 ){
				buf.append("<span style='color:#c6c6c8'>&nbsp;|&nbsp;</span>");
			}
		}
		buf.append("<br>");
		buf.append("<span style='font-weight:bold;font-size:11px; font-family:'Arial''>Description:&nbsp;&nbsp;</span>");
		buf.append("<span style='font-weight:normal;font-size:11px'>").append(_movie.getDescription()).append("</span><br>");
		return buf.toString();
	}
	
	@Override
	public int compareTo(ScoredMovie m){
		if(this._score == m._score){
			return 0;
		}
		return (this._score > m._score) ? 1 : -1;
	}
	
	public Movie getMovie(){
		return _movie;
	}
}
