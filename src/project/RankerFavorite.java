package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Map.Entry;

import project.SearchEngine.Options;

public class RankerFavorite extends Ranker{
	private float _betaRating = 1.0f;
	private float _betaYear = 1.0f;
	private float _betaNumReviews = 1.0f;
	private float _betaSimScore = 1.0f;
	private static ArrayList<Integer> RecommendationList= new ArrayList<Integer>();
	ArrayList<Integer> ActorID_List = new ArrayList<Integer>();
	ArrayList<Entry<Integer, Double>> Similarity_List = new ArrayList<Entry<Integer, Double>>();
	IndexerMovie _indexerMovie;
	
	protected RankerFavorite(Options options, Indexer indexer) {
		super(options, indexer);
		System.out.println("Using Ranker: " + this.getClass().getSimpleName());
		_betaRating=options._betaValues.get("beta_rat");
		_betaYear=options._betaValues.get("beta_yr");
		_betaNumReviews=options._betaValues.get("beta_numrev");
		_betaSimScore=options._betaValues.get("beta_simscore");
		_indexerMovie = (IndexerMovie)_indexer;
	}
	
	@Override
	public Map<ScoredMovie, ArrayList<Actor>> runQuery(Query query, int numResults, String mode) {    
		boolean exactmatchfound = false;
		Vector<String> queryV;
		Vector<ScoredMovie> all = new Vector<ScoredMovie>();
		ArrayList<Integer> movieList = new ArrayList<Integer>();
		Map<ScoredMovie, ArrayList<Actor>> resultMovies = new LinkedHashMap<ScoredMovie, ArrayList<Actor>>();
        ArrayList<Integer> ExactMatchActorL = new ArrayList<Integer>();
		queryV=query._tokens;
		RecommendationList.clear();
		// Search movies by Actor
		if(mode.equals("actor")){
			// Get actor IDs from query tokens
			ActorID_List = getActorIDList(queryV);
			// Exact match
			if(ActorID_List.size()>0){
				ExactMatchActorL.add(ActorID_List.get(0));
				exactmatchfound=true;
			}
		    if (ActorID_List.size() == query._tokens.size()){
		    	// Return the movie list all the actors were in
		    	movieList = _indexerMovie.getMoviesByActors(ActorID_List);
		    	for (int i = 0; i < movieList.size(); ++i) {
		    		all.add(scoreMovie(movieList.get(i)));//check if mid
		    	}
		    }
		    
		    // Similarity search
		    if (all.size()<1){
		    	ArrayList<Entry<Integer,Double>> ActorsArr = new ArrayList<Entry<Integer,Double>>();
		    	all.clear();
		    	ActorID_List.clear();
		    	//find similar actor names save in queryV
		    	for(String ActorName : queryV){
		    		ActorsArr=_indexerMovie.getTopMatches(ActorName, 1, 0.8, "actor");	
		    		if(ActorsArr.size()!=0){
		    			ActorID_List.add(ActorsArr.get(0).getKey());
		    		}
		    	}
		    	
		    	RecommendationList.addAll(ActorID_List);
		    	//get Movies common to all actors
		    	movieList.addAll(_indexerMovie.getMoviesByActors(ActorID_List));//get Movies common to all actors
		    	for (int i = 0; i < movieList.size(); ++i) {
		    		all.add(scoreMovie(movieList.get(i)));//check if mid
		    	}
		    }
		    
		    if (all.size()<1 && exactmatchfound){
		    	RecommendationList.clear();
		    	all.clear();
		    	movieList.clear();
		    	ActorID_List.clear();
		    	HashMap<Integer,Integer> actList = new HashMap<Integer,Integer>();
		    	movieList.addAll(_indexerMovie.getMoviesByActors(ExactMatchActorL));
		    	for (int mid : movieList)//get actors that worked with actors
		    	{
		    		for(int actid : _indexerMovie.getActorsByMovieID(mid))
		    		{
		    			if(actid!=ExactMatchActorL.get(0)){
		    			if(!actList.containsKey(actid)){
		    				actList.put(actid, 1);
		    			}
		    			else{
		    				actList.put(actid, actList.get(actid)+1);
		    			}
		    		}
		    		}
		    	}
		    	Entry<Integer,Integer> ActorMax = null;
		    	for (Entry<Integer,Integer> Actor : actList.entrySet()){
		    		if (ActorMax == null || Actor.getValue().compareTo(ActorMax.getValue()) > 0)
		    	    {
		    	        ActorMax = Actor;
		    	    }
		    	}
		    	movieList.clear();
		    	ActorID_List.addAll(ExactMatchActorL);
		    	ActorID_List.add(ActorMax.getKey());
		    	RecommendationList.addAll(ActorID_List);
		    	movieList.addAll(_indexerMovie.getMoviesByActors(ActorID_List));//get Movies common to all actors
		    	for (int i = 0; i < movieList.size() && movieList.size()>3; ++i) { //only if there are more than 3 movies per 
		    		all.add(scoreMovie(movieList.get(i)));//check if mid
		    	}
		    }
		    
		  //similarity search with monogram and display union of movies
		    if (all.size()<1){
		    	RecommendationList.clear();
		    	all.clear();
		    	queryV.clear();
		    	ActorID_List.clear();
		    	HashSet<Integer> MoviesSet = new HashSet<Integer>();
		    	ArrayList<Entry<Integer,Double>> ActorsArr = new ArrayList<Entry<Integer,Double>>();
		    	Scanner s = new Scanner(query._query);
				s.useDelimiter("\\s*(\\sand\\s|,|\\s)\\s*"); //split query by "and" or ,
			    while (s.hasNext()) {
			      queryV.add(s.next());
			    }
			    s.close();
		    	for(String ActorName : queryV){//find similar multiple actors names save in queryV
		    		ActorsArr = _indexerMovie.getTopMatches(ActorName, 3, 0.8, "actor");
		    		for(int i=0; i<ActorsArr.size();i++){//Add all similar actors
		    			ActorID_List.clear();
		    			ActorID_List.add(ActorsArr.get(i).getKey());
		    			movieList.addAll(_indexerMovie.getMoviesByActors(ActorID_List));
		    			MoviesSet.addAll(movieList);
		    			RecommendationList.addAll(ActorID_List);
		    		}
		    	}
		    	//return Results for Recommendation System
		    	for (int mid : MoviesSet) {//add the union of all movies to results
		    		all.add(scoreMovie(mid));
		    	}
		    }
		}
		
		//Search movies by Movie
		else if (mode.equals("movie")){
		    movieList = new ArrayList<Integer>();
		    double max = 0.0;
		    ArrayList<Double> SimilarityScore = new ArrayList<Double>();
		    for(Entry<Integer, Double> mov : _indexerMovie.getTopMatches(query._query, numResults, 0.7, "movie")){
				
				if(max==0.0){
					max=mov.getValue();
				}
				if(mov.getValue()==max){
					movieList.add(mov.getKey());
					SimilarityScore.add(mov.getValue());
				}
				else
				{
					RecommendationList.add(mov.getKey());
				}
			}
			for (int i = 0; i < movieList.size(); ++i) {
		    	all.add(scoreMovie(movieList.get(i),SimilarityScore.get(i)));//check if mid
		    }
		}
		
		Collections.sort(all, Collections.reverseOrder());
	    for (int i = 0; i < all.size() && i < numResults; ++i) {
	    	ScoredMovie sm = all.get(i);
	    	ArrayList<Integer> actorIDs = _indexerMovie.getActorsByMovieID(sm.getMovie().getId());
	    	ArrayList<Actor> actors = new ArrayList<Actor>();
	    	int range = 10;
	    	if(actorIDs.size() < 10){
	    		range = actorIDs.size();
	    	}
	    	for(int j = 0; j < range; j++){
	    		Actor actor = _indexerMovie.getActorDetails(actorIDs.get(j));
	    		actors.add(actor);
	    	}
	    	
	    	resultMovies.put(sm, actors);
	    }
	    
	    return resultMovies;
	  }

	
	public ArrayList<Integer> RecommendationListRetrieve(){
		return RecommendationList;
	}
	
	
	public ArrayList<String> Recommendation(Query query, String mode){
		if(mode.equals("actor")){
			return RecommendationA(query);
		}
		else{
			return RecommendationM(query);
		}
	}
	
	public ArrayList<String> RecommendationA(Query query){
		ArrayList<String> actors = new ArrayList<String>();
		ActorID_List = getActorIDList(query._tokens);
		// Exact Match, show the actors who worked with them most
		if(ActorID_List.size() == query._tokens.size()){
			ArrayList<Integer> actorsID = (ArrayList)_indexerMovie.getActorsWorkedMost(ActorID_List, -1);
			for(Integer actorID: actorsID){
				String actorname = _indexerMovie.getActorNameById(actorID);
				actors.add(actorname);
			}
		}
		// Show the similar actor name
		else{
			
		}
		
		return actors;
	}
	
	public ArrayList<String> RecommendationM(Query query){
		ArrayList<String> movies = new ArrayList<String>();
		return movies;
	}
	
	private ArrayList<Integer> getActorIDList(Vector<String> queryV) {
		  ActorID_List.clear();
		  for(String ActorName : queryV){
			  if(_indexerMovie.getActorIdByName(WordUtils.capitalizeFully(ActorName)) != null){
					ActorID_List.add(_indexerMovie.getActorIdByName(WordUtils.capitalizeFully(ActorName)));
				} 
			//ActorID_List.add(_indexerMovie.getActorIdByName(ActorName));
		  }
		  ActorID_List.remove(null);
		  return ActorID_List;
	  }
	  
	  private ScoredMovie scoreMovie(int mid) {
		  Double Rating;
		  Integer Year,NumRev;
		  Movie mov = _indexerMovie.getMovieDetails(mid);
		  Rating=mov.getRating();
		  Year=Integer.parseInt(mov.getYear());
		  NumRev=mov.getRatingsCount();
		  double score = _betaRating*Rating+_betaYear*Year+_betaNumReviews*NumRev;
		  return new ScoredMovie(mov, score);
	  }
	  
	  private ScoredMovie scoreMovie(Integer mid, Double simScore) {
		  Double Rating;
		  Integer Year,NumRev;
		  Movie mov = _indexerMovie.getMovieDetails(mid);
		  Rating=mov.getRating();
		  Year=Integer.parseInt(mov.getYear());
		  NumRev=mov.getRatingsCount();
		  double score = _betaRating*Math.tanh(Rating/10.0)+_betaYear*(Year/15.0)+_betaNumReviews*Math.tanh(NumRev/100000.0)+_betaSimScore*simScore;
		  score/=(_betaRating+_betaYear+_betaNumReviews+_betaSimScore);
		  return new ScoredMovie(mov, score);
	  }

	@Override
	public ArrayList<Movie> getMoviesByList(ArrayList<Integer> list) {
		ArrayList<Movie> ListofM = new ArrayList<Movie>();
		for(int movid : list){
			ListofM.add(_indexerMovie.getMovieDetails(movid));
		}
		return ListofM;
	}

	@Override
	public ArrayList<Actor> getActorsByList(ArrayList<Integer> list) {
		ArrayList<Actor> ListofA = new ArrayList<Actor>();
		for(int actid : list){
			ListofA.add(_indexerMovie.getActorDetails(actid));
		}
		return ListofA;
	}
	  
	  
}
