import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class BA1F {

	public static void main(String args[]) {
		
		File file = new File("");
		
		BufferedReader read;
		try {
			String currentPath = file.getAbsolutePath();
			read = new BufferedReader(new FileReader(currentPath + "/input"));
			String line = read.readLine();
			String compiledString = "";
			
			
			int countPos[] = new int[line.length()+1];
			int counter = 0;
			
			
			while(line != null) {
				compiledString = compiledString + line;
				line = read.readLine();
			}
			read.close();
			
			for(int i = 0; i < compiledString.length(); i++) {
				if(compiledString.charAt(i) == 'C') {
					counter = counter - 1;
					countPos[i] = counter;
				
				}else if(compiledString.charAt(i) == 'G') {
					counter = counter + 1; 
					countPos[i] = counter;
				
				}else if(compiledString.charAt(i) == 'A' || compiledString.charAt(i) == 'T') {
					countPos[i] = counter;
			
					continue;
				}
			}
			
			String result = "";
		
			int min = countPos[0];
			for(int i = 0; i < countPos.length; i++) {
				if(countPos[i] < min) {
					min = countPos[i];
				}
			}
			
			
			for(int i = 0; i < countPos.length; i++) {
				if(countPos[i] == min) {
					result = result + String.valueOf(i + 1) + " ";
				}
			}
			
			result = result.trim();
			Path fName = Path.of(currentPath + "/output");
			Files.writeString(fName, result);
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
