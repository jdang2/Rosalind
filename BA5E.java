import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;


public class BA5E {
	public static void main(String args[]) {
		File file = new File("");
		BufferedReader read;
		BufferedReader matrix;
		try {
			String currentPath = file.getAbsolutePath();
			
			//Deals with the two strings in input
			read = new BufferedReader(new FileReader(currentPath + "/input"));
			String line = read.readLine();
			boolean firstString = true;
			String stringOne = "";
			String stringTwo = "";
			while(line != null) {
				if(firstString) {
					stringOne = line;
					firstString = false;
				}else {
					stringTwo = line;
				}
				line = read.readLine();
			}
			read.close();
			
			//Deals with the matrix in BLOSUM62
			matrix = new BufferedReader(new FileReader(currentPath + "/BLOSUM62.txt"));
			String l = matrix.readLine();
			boolean firstLine = true;
			ArrayList<String> alphabetIndex = new ArrayList<String>(20);
			boolean negative = false;
			String doubleDigit = "";
			boolean doubleDig = false;
			int [][] scoreMatrix = new int [20][20];
			int colCount = 0;
			int rowCount = 0;
			
			while(l != null) {
				
				if(firstLine) {
					for(int i = 0; i < l.length(); i++) {
						if(l.charAt(i) == ' ') {
							continue;
						}else {
							alphabetIndex.add(Character.toString(l.charAt(i)));
						}
					}
					firstLine = false;
				}else {
					 
					for(int i = 2; i < l.length(); i++) {
						String current = Character.toString(l.charAt(i));
						
						if(current.equals(" ")) {
							continue;
						}
						
						if(current.equals("-")) {
							negative = true;
							continue;
						}
						
						if(i != l.length() - 1 && l.charAt(i+1) != ' ') {
							doubleDig = true;
							doubleDigit = current;
							continue;
						}
						
						if(doubleDig) {
							current = doubleDigit + current;
							doubleDig = false;
						}
						
						if(negative) {
							scoreMatrix[rowCount][colCount++] = Integer.parseInt(current) * -1;
							negative = false;
						}else {
							scoreMatrix[rowCount][colCount++] = Integer.parseInt(current);
							
						}
						
					}
					rowCount++;
					colCount = 0;
					
				}
				
				
				l = matrix.readLine();
			}
			matrix.close();
		
			
				
			String result = "";
			int [][] matrixCheck = stringScore(stringOne, stringTwo, alphabetIndex, scoreMatrix);
			int score = matrixCheck[matrixCheck.length - 1][matrixCheck[matrixCheck.length-1].length - 1];
			ArrayList<String> lineOne = new ArrayList<String>();
			ArrayList<String> lineTwo = new ArrayList<String>();
			
			int i = stringTwo.length() - 1;
			int j = stringOne.length() - 1;
			
			while(i != -1 || j != -1) {
				int storeRow = 0;
				int storeCol = 0;
				int current = 0;
				
				if(i == -1) {
					storeRow = alphabetIndex.indexOf(Character.toString(stringTwo.charAt(stringTwo.length()-1)));
					storeCol = alphabetIndex.indexOf(Character.toString(stringOne.charAt(j)));
					current = matrixCheck[i+1][j+1] - scoreMatrix[storeRow][storeCol];
				}
				
				if(j == -1) {
					storeRow = alphabetIndex.indexOf(Character.toString(stringTwo.charAt(i)));
					storeCol = alphabetIndex.indexOf(Character.toString(stringOne.charAt(stringOne.length()-1)));
					current = matrixCheck[i+1][j+1] - scoreMatrix[storeRow][storeCol];
				}
				
				if(j != -1 && i != -1) {
					storeRow = alphabetIndex.indexOf(Character.toString(stringTwo.charAt(i)));
					storeCol = alphabetIndex.indexOf(Character.toString(stringOne.charAt(j)));
					current = matrixCheck[i+1][j+1] - scoreMatrix[storeRow][storeCol];
				}
			  
				
				
				if(i == -1) {
					if(current == matrixCheck[stringTwo.length()-1][j]) {
						lineOne.add(Character.toString(stringOne.charAt(j)));
						lineTwo.add(Character.toString(stringTwo.charAt(stringTwo.length()-1)));
						i = -1;
						j--;
					}else {
						current = Math.max(matrixCheck[i+1][j], matrixCheck[i+1][j+1]);
						if(current == matrixCheck[i+1][j]) {
							lineOne.add(Character.toString(stringOne.charAt(j)));
							lineTwo.add("-");
							j--;
						}else {
							lineTwo.add(Character.toString(stringTwo.charAt(stringTwo.length()-1)));
							lineOne.add("-");
							i = -1;
						}
					}
				}else if(j == -1){
					if(current == matrixCheck[i][stringOne.length()-1]) {
						lineOne.add(Character.toString(stringOne.charAt(stringOne.length()-1)));
						lineTwo.add(Character.toString(stringTwo.charAt(i)));
						i--;
						j = -1;
					}else {
						current = Math.max(matrixCheck[i+1][j+1], matrixCheck[i][j+1]);
						if(current == matrixCheck[i+1][j+1]) {
							lineOne.add(Character.toString(stringOne.charAt(stringOne.length()-1)));
							lineTwo.add("-");
							j = -1;
						}else {
							lineTwo.add(Character.toString(stringTwo.charAt(i)));
							lineOne.add("-");
							i--;
						}
					}
			
			}else {
				if(current == matrixCheck[i][j]) {
					lineOne.add(Character.toString(stringOne.charAt(j)));
					lineTwo.add(Character.toString(stringTwo.charAt(i)));
					i--;
					j--;
				}else {
					current = Math.max(matrixCheck[i+1][j], matrixCheck[i][j+1]);
					if(current == matrixCheck[i+1][j]) {
						lineOne.add(Character.toString(stringOne.charAt(j)));
						lineTwo.add("-");
						j--;
					}else {
						lineTwo.add(Character.toString(stringTwo.charAt(i)));
						lineOne.add("-");
						i--;
					}
				}
				}
			}
			
			Collections.reverse(lineOne);
			Collections.reverse(lineTwo);
			result = Integer.toString(score) + "\n";
			for(int a = 0; a < lineOne.size();a++) {
				result = result + lineOne.get(a);
			}
			result = result + "\n";
			for(int a = 0; a < lineTwo.size();a++) {
				result = result + lineTwo.get(a);
			}
			
			result = result.trim();
			Path fName = Path.of(currentPath + "/output");
			Files.writeString(fName, result);
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int[][] stringScore(String stringOne, String stringTwo, ArrayList<String> alphabetIndex, int [][] scoreMatrix) {
		final int sigma = 5;
		int strLenOne = stringOne.length();
		int strLenTwo = stringTwo.length();
		
		int [][] strMatrix = new int[strLenTwo + 1][strLenOne + 1];
		
		for(int i = 0; i < strLenOne; i++) {
			strMatrix[0][i+1] = strMatrix[0][i] - sigma;
		}
		
		for(int i = 0; i < strLenTwo; i++) {
			strMatrix[i+1][0] = strMatrix[i][0] - sigma;
		}
		
		for(int row = 0; row < strLenTwo; row++) {
			
			for(int col = 0; col < strLenOne; col++) {
				int down = strMatrix[row][col+1] - sigma;
				int right = strMatrix[row + 1][col] - sigma;
				
				int storeRow = alphabetIndex.indexOf(Character.toString(stringTwo.charAt(row)));
				int storeCol = alphabetIndex.indexOf(Character.toString(stringOne.charAt(col)));
				int current = strMatrix[row][col] + scoreMatrix[storeRow][storeCol];
				
				int max = max(down, right, current);
				strMatrix[row + 1][col + 1] = max;
				
			}
		}
		

		return strMatrix;
	}
	
	public static int max(int one, int two, int three) {
		int result = 0;
		
		if(one >= two && one >= three) {
			result = one;
		}else if(two >= one && two >= three) {
			result = two;
		}else {
			result = three;
		}
		
		return result;
	}
	
}
