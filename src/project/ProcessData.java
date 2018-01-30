package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ProcessData{
	
	static String corpusfile = "/Users/jinfanyang/Desktop/Project/Project/data/simple/imdbmovielinks.txt";
	static String targetfile = "/Users/jinfanyang/Desktop/Project/Project/data/simple/imdbmovielinks.csv";
	
	public static void constructFile() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(corpusfile));
		PrintWriter pw = new PrintWriter(new File(targetfile));
		pw.write("Name,Year,Rate,Director,Actor1,Actor2,Actor3\n");
		
		try {
		      String line = null;
		      int i=1;
		      String s1="";
		      String s2="";
		      while ((line = br.readLine()) != null) {
		    	  if(i==1){
		    		  s1 = getBasicInformation(line);
		    		  i+= 1;
		    	  }
		    	  else if(i==3){
		    		  s2 = getActors(line);
		    		  i = 1;
		    		  String s = s1 + s2+"\n";
		    		  pw.write(s);
		    	  }
		    	  else{
		    		  i+= 1;
		    	  }
		      }
		      
		    } finally {
		      br.close();
		      pw.close();
		    }
	}
	
	public static String getBasicInformation(String line){
		Scanner s = new Scanner(line).useDelimiter("\t");
		StringBuilder sb = new StringBuilder();
		int i = 1;
		while(s.hasNext()){
			String P = s.next();
			if(i==1 || i==4){
				P=P.replaceAll("[^a-zA-Z0-9]", "");
			}
			System.out.println(P);
			if(i< 5){
				sb.append(P);
				sb.append(",");
			}
			i += 1;
		}
		return sb.toString();
	}
	
	public static String getActors(String line){
		Scanner s = new Scanner(line).useDelimiter("\t");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while(s.hasNext()){
			if(i < 3){
				sb.append(s.next().replaceAll("[^a-zA-Z0-9]",""));
				if(i<2){
					sb.append(",");
				}
				i += 1;
			}
			else{
				break;
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws IOException{
		constructFile();
	}

}
