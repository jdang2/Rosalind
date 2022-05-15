import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;


public class BA8E {
	public static void main(String args[]) {
		
		File file = new File("");
		boolean firstLine = true;
		ArrayList<Double> listOfPoints = new ArrayList<>();
		BufferedReader read;
		try {
			String currentPath = file.getAbsolutePath();
			read = new BufferedReader(new FileReader(currentPath + "/input.txt"));
			String line = read.readLine();
			int n = 0;
			
			while(line != null) {
				//Get the size of the matrix
				if(firstLine) {
					n = Integer.valueOf(line);
					firstLine = false;
				}else {
					//Get everything else
					String temp = "";
					for(int i = 0; i < line.length(); i++) {
						if(line.charAt(i) != ' ') {
							temp = temp + line.charAt(i);
						}else if(line.charAt(i) == ' ') {
							listOfPoints.add(Double.valueOf(temp));
							temp = "";
						}if(i == line.length()-1) {
							listOfPoints.add(Double.valueOf(temp));
							temp = "";
						}
						
					}
				}
				line = read.readLine();
			}
			read.close();
			
			//Make the distance matrix from the txt file
			double [][] distanceMatrix = new double[n][n];
			for(int row = 0; row < n; row++) {
				for(int col = 0; col < n; col++) {
					distanceMatrix[row][col] = listOfPoints.get(0);
					listOfPoints.remove(0);
				}
			}
			
			// n-1 single-element clusters labeled 0, ... , n-1
			ArrayList<int[]> clusters = new ArrayList<>();
			for(int i = 0; i < n; i++) {
				int[] temp = new int[1];
				temp[0] = i;
				clusters.add(temp);
			}
			
			ArrayList<int[]> combinedClusters = new ArrayList<>();
			int closestRow = 0;
			int closestCol = 0;
			
			//while there is more than one cluster 
			while(clusters.isEmpty() == false) {
				double minimumDistance = 99999;
				
				if(combinedClusters.size() == 6) {
					System.out.println(";");
				}
				
				for(int i = 0; i < clusters.size()-1; i++) {
					for(int j = i + 1; j < clusters.size(); j++) {
						double currentDistance = 0;
						int[] ks = clusters.get(i);
						for (int k = 0; k < ks.length; k++) {
							int currentRow = ks[k];
							int[] ls = clusters.get(j);
							for (int l = 0; l < ls.length; l++) {
								int currentCol = ls[l];
								currentDistance += distanceMatrix[currentRow][currentCol];
							}
						}
						//get distance
						currentDistance = currentDistance / ((clusters.get(i).length) * (clusters.get(j).length));
						//find the two closest clusters
						if(currentDistance < minimumDistance) {
							minimumDistance = currentDistance;
							closestRow = i;
							closestCol = j;
						}
					}
				}
				
				//merge Ci and Cj into a new cluster
				if(clusters.size() >= 2) {
					int[] combined = Arrays.copyOf(clusters.get(closestRow), clusters.get(closestRow).length + clusters.get(closestCol).length);
					System.arraycopy(clusters.get(closestCol), 0, combined, clusters.get(closestRow).length, clusters.get(closestCol).length);
					
					for(int iter = 0; iter < clusters.size(); iter++) {
						for(int currentNum : combined) {
							if(check(clusters.get(iter), currentNum)) {
								clusters.remove(iter);
								iter--;
								break;
							}
						}
					}
					
					clusters.add(combined);
					combinedClusters.add(combined);
				}else {
					clusters.remove(0);
				}
				
	
			}
			
			String result = "";
			//add 1 to clusters because 0-indexing
			for(int[] currentCluster : combinedClusters) {
				String temp = "";
				for(int i = 0; i < currentCluster.length; i++) {
					temp = temp + String.valueOf(currentCluster[i] + 1) + " ";
				}
				temp = temp.trim();	
				result += temp + "\n";
			}
			
			 
			result = result.trim();
			Path fName = Path.of(currentPath + "/output.txt");
			Files.writeString(fName, result);
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//method to check if an array has a number
    private static boolean check(int[] arr, int num){

        boolean hasEle = false;
        for (int element : arr) {
            if (element == num) {
            	hasEle = true;
                break;
            }
        }
        
        return hasEle;
    }
	
}
