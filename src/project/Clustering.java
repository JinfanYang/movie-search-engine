package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.clusterers.*;

public class Clustering {
	
	static String file = "/Users/jinfanyang/Desktop/Project/Project/data/simple/imdbmovielinks.csv";

	public static void cluster() throws Exception{
		//BufferedReader reader = new BufferedReader(new FileReader(file));
		
		//Instances data = new Instances(reader);
		//reader.close();
		
		DataSource source = new DataSource(file);
		Instances data = source.getDataSet();
		//if (data.classIndex() == -1)
			  //data.setClassIndex(data.numAttributes() - 1);
		
		String[] options = new String[2];
		options[0] = "-I";
		options[1] = "100";
		
		EM cluster = new EM();
		cluster.setOptions(options);
		cluster.buildClusterer(data);
		
		ClusterEvaluation eval = new ClusterEvaluation();
	    eval.setClusterer(cluster);
	    eval.evaluateClusterer(new Instances(data));
	    System.out.println("# of clusters: " + eval.getNumClusters());
	}
	
	public static void main(String[] args) throws Exception{
		cluster();
	}
}
