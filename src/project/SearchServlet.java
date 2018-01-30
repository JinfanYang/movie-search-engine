package project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/Search")
public class SearchServlet extends HttpServlet {
	
	SearchEngine se;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String query = request.getParameter("query");
		String mode = request.getParameter("mode");
		try {
			if(se == null){
				se = new SearchEngine();
			}
			
			IndexerMovie index = (IndexerMovie) se.indexer;
			
			String ip = request.getRemoteAddr();
			if (ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
			    InetAddress inetAddress = InetAddress.getLocalHost();
			    String ipAddress = inetAddress.getHostAddress();
			    ip = ipAddress;
			}
			System.out.println(ip);
			
			// Write log
			FileWriter fw = new FileWriter(se.OPTIONS._corpusPrefix+"/searchlog.log", true);
			String queryinfo = query.toLowerCase();
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(mode+"\t"+queryinfo+"\t"+ System.currentTimeMillis() + "\t" + ip + "\n");
			bw.flush();
			bw.close();
			
			Query processedQuery = new Query(query, mode);
			processedQuery.processQuery();
			
			Ranker ranker = Ranker.Factory.getRanker(se.OPTIONS, se.indexer);
			
			// Ranking.
			Map<ScoredMovie, ArrayList<Actor>> scoredMovie = ranker.runQuery(processedQuery, 10, mode);
			
			// Recommendation
			ReadLog rl = new ReadLog(se.OPTIONS);
			ArrayList<String> list = rl.getList(mode, queryinfo);
			ArrayList<String> listByUser = rl.getListByUser(mode, queryinfo, ip);
			ArrayList<Integer> recomlist = ranker.RecommendationListRetrieve();
			
			if(recomlist.isEmpty()){
				if(mode.equals("actor")){
					ArrayList<String> workMost = ranker.Recommendation(processedQuery, mode);
					ArrayList<Actor> recommendation = rl.combineA(list, listByUser, workMost, index);
					request.getSession().setAttribute("Recommen", CreateHTML.createAHTML(recommendation));
				}
				else{
					ArrayList<String> movies = ranker.Recommendation(processedQuery, mode);
					ArrayList<Movie> recommendation = rl.combineM(list, listByUser, movies, index);
					request.getSession().setAttribute("Recommen", CreateHTML.createMHTML(recommendation));
				}
				request.getSession().setAttribute("BestGuess", "");
			}
			else{
				if(mode.equals("actor")){
						ArrayList<Actor> actorsList = new ArrayList<Actor>();
						actorsList = ranker.getActorsByList(recomlist);
						if(actorsList.size() == 1){
							request.getSession().setAttribute("BestGuess", actorsList.get(0).getName());
						}
						else{
							request.getSession().setAttribute("Recommen", CreateHTML.createAHTML(actorsList));
						}
						
					}
					else{
						ArrayList<Movie> moviesList = new ArrayList<Movie>();
						moviesList = ranker.getMoviesByList(recomlist);
						if(moviesList.size() == 1){
							request.getSession().setAttribute("BestGuess", moviesList.get(0).getName());
						}
						else{
							request.getSession().setAttribute("Recommen", CreateHTML.createMHTML(moviesList));
						}
					}
			}
			
			// UI
			if (scoredMovie != null && scoredMovie.size()>0){
				StringBuilder sb = new StringBuilder();				
				for(Entry<ScoredMovie, ArrayList<Actor>> entry : scoredMovie.entrySet()){
					ScoredMovie sm = entry.getKey();
					ArrayList<Actor> actorList = entry.getValue();
					sb.append("<div>");
					sb.append(sm.asHtmlResult());
					sb.append("<span style='font-weight:bold;font-size:11px'>Cast:&nbsp;&nbsp;</span>");
					
					for(int i = 0; i<actorList.size(); i++){
						if(actorList.get(i).getWikiUrl() == null){
							sb.append("<span style='font-weight:normal;font-size:11px'>");
							sb.append(actorList.get(i).getName()).append("</span>");
						}
						else{
							sb.append("<a href="+actorList.get(i).getWikiUrl()+"><span style='font-weight:normal;font-size:11px'>");
							sb.append(actorList.get(i).getName()).append("</span></a>");
						}	
						if(i < actorList.size()-1 ){
							sb.append("<span style='color:#c6c6c8'>&nbsp;|&nbsp;</span>");
						}
						else{
							sb.append("...");
						}
					}
					
					sb.append("<br>");
					sb.append("</div></div>");
					sb.append("</div>");
				}
				request.getSession().setAttribute("ResponseBody", sb);
				request.getSession().setAttribute("Query", query);
				request.getSession().setAttribute("Founded", 1);
				request.getSession().setAttribute("Mode", mode);
				request.getRequestDispatcher("Result.jsp").forward(request, response);
			}
			
			
			
			else{
				request.getSession().setAttribute("Founded", 0);
				request.getRequestDispatcher("Result.jsp").forward(request, response);
			}
			
			//request.getSession().invalidate();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
