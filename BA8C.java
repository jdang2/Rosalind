import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BA8C {
	public static void main(String args[]) {

		File file = new File("");

		BufferedReader read;
		try {
			String currentPath = file.getAbsolutePath();
			read = new BufferedReader(new FileReader(currentPath + "/input.txt"));
			String line = read.readLine();
			boolean isHeader = true;
			String strK = "empty";
			String strN = "empty";
			ArrayList<String[]> inputingPoints = new ArrayList<String[]>();
			
			while(line != null) {
				String temp = "";
				
				if(isHeader) {
					for(int i = 0; i < line.length(); i++) {
						if(line.charAt(i) != ' ') {
							temp = temp + line.charAt(i);

						}if((line.charAt(i) == ' ')) {
							if(strK.equals("empty")) {
								strK = temp;
								temp = "";
							}
						}else if(i == line.length()-1) {
							if(strN.equals("empty")) {
								strN = temp;
								temp = "";
							}
						}
					}
				}
				if(!isHeader) {
					String [] curr = (line.split(" "));
					inputingPoints.add(curr);
				}
				line = read.readLine();
				isHeader = false;
			}
			read.close();

			ArrayList<float[]> dataPoints = new ArrayList<float[]>();
			
			for (int j = 0; j < inputingPoints.size(); j++) {
				String[] currentClust = inputingPoints.get(j);
				float [] tempArr = new float[currentClust.length];
				for (int i = 0; i < currentClust.length; i++) {
					tempArr[i] = (float) Double.parseDouble(currentClust[i]);
				}
				dataPoints.add(tempArr);
			}
			
			int k = Integer.parseInt(strK);
			int n = Integer.parseInt(strN);
			inputingPoints.clear();
			
			ArrayList<float[]> finalResult = new ArrayList<float[]>();
			finalResult = lloyd(k, dataPoints, n);
			String result = "";
			for (int j = 0; j < finalResult.size(); j++) {
				float[] point = finalResult.get(j);
				for(int i = 0; i < point.length; i++) {
					result = result + (point[i]) + " ";
				}
				result = result.trim();
				result = result + "\n";
			}
			 

			result = result.trim();
			Path fName = Path.of(currentPath + "/output.txt");
			Files.writeString(fName, result);


		}catch(IOException e) {
			e.printStackTrace();
		}


	}
	
	public static float euclidean(float[] a, float[] b){
		float currDistance = 0;
		for(int i = 0; i < a.length; i++) {
			currDistance += Math.pow((a[i] - b[i]), 2);
		}
		currDistance = (float) Math.sqrt(currDistance);
		return currDistance;
	}
	
	
	
	public static ArrayList<float[]> lloyd(int k, ArrayList<float[]>dataPoints, int n){
		ArrayList<float[]> centerPoints = new ArrayList<float[]>();
		for(int i = 0; i < k; i++) {
			//initial guess
			centerPoints.add(dataPoints.get(i));
		}
		HashMap<float[], ArrayList<float[]>> clusterAssign = new HashMap<>();
		
		while(true) {
			for (int i = 0; i < dataPoints.size(); i++) {
				float[] currentPointSet = dataPoints.get(i);
				float [] closestCenter = findCloseCenter(currentPointSet, centerPoints);
				
				//assign points to center
				if(clusterAssign.get(closestCenter) == null) {
					clusterAssign.put(closestCenter, new ArrayList<float[]>());
					clusterAssign.get(closestCenter).add(currentPointSet);
				}else {
					clusterAssign.get(closestCenter).add(currentPointSet);
				}
			}
			
			ArrayList<float[]> realignCenter = new ArrayList<float[]>();

			
			for(int i = 0; i < k; i++) {
				//reassign using average of points
				float[] realign = averagePoints(clusterAssign.get(centerPoints.get(i)), n);
				realignCenter.add(realign);
			}
			
			int count = 0;
			for(int i = 0; i < centerPoints.size(); i++) {
				if(Arrays.equals(centerPoints.get(i), realignCenter.get(i))) {
					count++;
				}
			}
			
			if(count == k) {
				break;
			}
		
			centerPoints = realignCenter;
			
		}
		clusterAssign.clear();
		return centerPoints;
	}
	
	public static float[] findCloseCenter(float[] currentPoint, ArrayList<float[]> centerPoints){
		float min = 99999;
		float[] result = new float[currentPoint.length];
		
		for (int i = 0; i < centerPoints.size(); i++) {
			float[] currentCenter = centerPoints.get(i);
			float currentDistance = euclidean(currentCenter, currentPoint);
			if(currentDistance < min) {
				min = currentDistance;
				result = currentCenter;
			}
		}
		return result;
	}
	
	public static float[] averagePoints(ArrayList<float[]> clusterPoints, int n) {
		float[] newCenter = new float[n];
		
		for (int j = 0; j < clusterPoints.size(); j++) {
			float[] currentPoint = clusterPoints.get(j);
			for(int i = 0; i < n; i++) {
				newCenter[i] += currentPoint[i];
			}
		}
		
		for(int i = 0; i < n; i++) {
			newCenter[i] = newCenter[i]/clusterPoints.size();
		}
		
		return newCenter;
		
	}

}
