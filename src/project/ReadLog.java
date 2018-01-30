package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import project.SearchEngine.Options;

public class ReadLog {
	Options _options;
	public ReadLog(Options options){
		_options = options;
	}
	
	public ArrayList<String> getList(String mode, String queryinfo) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		String logfile = _options._corpusPrefix+"/searchlog.log";
		BufferedReader reader = new BufferedReader(new FileReader(logfile));
		String line = null;
		String[] loginfo = null;
		
		while ((line = reader.readLine()) != null) {
			loginfo = line.split("\t");
			if(loginfo[0].equals(mode)){
				if(loginfo[1].equals(queryinfo)){
					long time = Long.parseLong(loginfo[2]);
					long timelag = 0;
					while(timelag < 1800000){
						line = reader.readLine();
						if(line != null){
							loginfo = line.split("\t");
							long nexttime = Long.parseLong(loginfo[2]);
							timelag = nexttime - time;
							if(loginfo[0].equals(mode)){
								if(!loginfo[1].equals(queryinfo)){
									list.add(loginfo[1]);
								}
							}
						}
						else{
							break;
						}
					}
				}
			}
	    }
		reader.close();
		
		return list;
	}
	
	public ArrayList<String> getListByUser(String mode, String queryinfo, String ip) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		String logfile = _options._corpusPrefix+"/searchlog.log";
		BufferedReader reader = new BufferedReader(new FileReader(logfile));
		String line = null;
		String[] loginfo = null;
		
		while ((line = reader.readLine()) != null) {
			loginfo = line.split("\t");
			if(loginfo[3].equals(ip)){
				if(loginfo[0].equals(mode)){
					if(loginfo[1].equals(queryinfo)){
						long time = Long.parseLong(loginfo[2]);
						long timelag = 0;
						while(timelag < 1800000){
							line = reader.readLine();
							if(line != null){
								loginfo = line.split("\t");
								if(loginfo[3].equals(ip)){
									if(loginfo[0].equals(mode)){
										if(!loginfo[1].equals(queryinfo)){
											long nexttime = Long.parseLong(loginfo[2]);
											timelag = nexttime - time;
											list.add(loginfo[1]);
										}
									}
								}
							}
							else{
								break;
							}
						}
					}
				}
			}
	    }
		reader.close();
		return list;
	}
	
	public ArrayList<Actor> combineA(ArrayList<String> a, ArrayList<String> b, ArrayList<String> c, IndexerMovie indexer){
		ArrayList<Actor> all = new ArrayList<Actor>();
		all = readListA(all, a, indexer);
		all = readListA(all, b, indexer);
		all = readListA(all, c, indexer);
		return all;
	}
	
	private ArrayList<Actor> readListA(ArrayList<Actor> actors, ArrayList<String> list, IndexerMovie indexer){
		Integer actorId = null;
		for(int i = 0; i<list.size(); i++){
				actorId = indexer.getActorIdByName(list.get(i));
				if(actorId != null){
					actors.add(indexer.getActorDetails(actorId));
				}
		}
		return actors;
	}
	
	public ArrayList<Movie> combineM(ArrayList<String> a, ArrayList<String> b, ArrayList<String> c, IndexerMovie indexer){
		ArrayList<Movie> all = new ArrayList<Movie>();
		all = readListM(all, a, indexer);
		all = readListM(all, b, indexer);
		all = readListM(all, c, indexer);
		return all;
	}
	
	private ArrayList<Movie> readListM(ArrayList<Movie> movies, ArrayList<String> list, IndexerMovie indexer){
		Integer movieId = null;
		for(int i = 0; i<list.size(); i++){
				movieId = indexer.getMovieIdByName(list.get(i));
				if(movieId != null){
					movies.add(indexer.getMovieDetails(movieId));
				}
		}
		return movies;
	}
		
}
