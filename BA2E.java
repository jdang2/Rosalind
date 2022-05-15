import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class BA2E {

	public static void main(String[] args) {
		File file = new File("");
		
		BufferedReader read;
		try {
			String currentPath = file.getAbsolutePath();
			read = new BufferedReader(new FileReader(currentPath + "/input.txt"));
			String line = read.readLine();
			int first = 0;
			int k = 0;
			int t = 0;
			
			ArrayList<String> dnaList = new ArrayList<String>();	
			ArrayList<String> motifs = new ArrayList<String>();	
			
			while(line != null) {
				if(first == 0) {
					k = Integer.valueOf(line.substring(0, 1));
					t = Integer.valueOf(line.substring(2));
					first++;
				}else {
					 dnaList.add(line);
				}
				line = read.readLine();
			}
			read.close();
			
			for(String i : dnaList) {
				motifs.add(i.substring(0, k));
			}
			
			motifs = actualBestMotifs(motifs, dnaList, k, t);
			 
			String result = "";
			for(int i = 0; i < motifs.size(); i++) {
				result = result + motifs.get(i) + "\n";
			}
			result = result.trim();
			System.out.print(result);
			Path fName = Path.of(currentPath + "/output.txt");
			Files.writeString(fName, result);
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}

	}
	
	public static int scoreMotif(ArrayList<String> list) {
		int score = 0;
		
		for(int col = 0; col < list.get(0).length();col++) {
			int a = 0;
			int c = 0;
			int g = 0;
			int t = 0;
			for(int row = 0; row < list.size(); row++) {
				if(list.get(row).charAt(col) == 'A') {
					a++;
				}else if(list.get(row).charAt(col) == 'C') {
					c++;
				}else if(list.get(row).charAt(col) == 'G') {
					g++;
				}else if (list.get(row).charAt(col) == 'T') {
					t++;
				}
			}
			int temp = a;
			if(c > temp) {
				temp = c;
			}
			if(g > temp) {
				temp = g;
			}
			if(t > temp) {
				temp = t;
			}
			score = (score + list.size()) - temp;
		}
		
		return score;
	}
	
	public static ArrayList<String> actualBestMotifs(ArrayList<String> motifs, ArrayList<String> dna, int k, int t){
		int score = scoreMotif(motifs);
		
		for(int i = 0; i < dna.get(0).length() - k + 1; i++) {
			ArrayList<String> trackMotif = new ArrayList<String>();
			
			String temp = dna.get(0).substring(i, i + k);
			trackMotif.add(temp);
			
			for(int o = 1; o < t; o++) {
				double [][] profile = profileMatrix(trackMotif);
				String motif = mostProbable(profile, dna.get(o), k);
				trackMotif.add(motif);
			}
			
			int tempScore = scoreMotif(trackMotif);
			
			if(tempScore < score){			
				score = tempScore;
				motifs = new ArrayList<String>(trackMotif);
			}
			
		}
		
		return motifs;
	}
	
	public static double[][] profileMatrix(ArrayList<String> motif){
		double[][] profile = new double[4][motif.get(0).length()];
		
		for(int col = 0; col < motif.get(0).length(); col++) {
			double a = 1;
			double c = 1;
			double g = 1;
			double t = 1;
			
			for(int row = 0; row < motif.size(); row++) {
				if(motif.get(row).charAt(col) == 'A') {
					a++;
				}else if(motif.get(row).charAt(col) == 'C') {
					c++;
				}else if(motif.get(row).charAt(col) == 'G') {
					g++;
				}else if (motif.get(row).charAt(col) == 'T') {
					t++;
				}
				
			}
			profile[0][col] = a/motif.size();;
			profile[1][col] = c/motif.size();;
			profile[2][col] = g/motif.size();;
			profile[3][col] = t/motif.size();;
				
				
				
			}
			
		return profile;
	}
	
	private static double scoreProfile(String dna, double[][] profile) {
		double score = 1;
			
		for(int i = 0; i < dna.length(); i++){
			
			double probability = 0;
			if(dna.charAt(i) == 'A') {
				probability = profile[0][i];
			}else if(dna.charAt(i) == 'C') {
				probability = profile[1][i];
			}else if(dna.charAt(i) == 'G') {
				probability = profile[2][i];
			}else if (dna.charAt(i) == 'T') {
				probability = profile[3][i];
			}
					
			score = score * probability;	
		}			
		return score;	
	}
	
	
	public static String mostProbable(double [][] profile, String dna, int k) {
		
		String sub = dna.substring(0, k);
		double score = scoreProfile(sub, profile);
		
		for(int i = 1; i < dna.length() - k + 1; i++){
			
			String current = dna.substring(i, i + k);
			
			double prob = scoreProfile(current, profile);
			if(prob > score){
				
				sub = current;
				score = prob;
			}
		}
		return sub;
		
	}

}
