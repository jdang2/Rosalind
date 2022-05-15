import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;



public class BA1D {

	
	public static void zAlgo() {
		File file = new File("");
		long start = System.currentTimeMillis();

		BufferedReader read;
		try {
			String currentPath = file.getAbsolutePath();
			read = new BufferedReader(new FileReader(currentPath + "/input.txt"));
			String line = read.readLine();

			int first = 0;
			String result = "";
			String compiledString = "";
			String pattern = "";
			
			while(line != null) {
				if(first == 0) {
					pattern = line;
					first++;
				}else {
					compiledString = compiledString + line;
				}
				
				line = read.readLine();
			}
			
			read.close();
			
			String concat = pattern + "$" + compiledString;
			 
	        int l = concat.length();
	 
	        int Z[] = new int[l];
	 
	        getZarr(concat, Z);
	 

						
			result = result.trim();
			Path fName = Path.of(currentPath + "/output.txt");
			Files.writeString(fName, result);
			long end = System.currentTimeMillis();

			long elapsedTime = end - start;
			double intoSeconds = elapsedTime * 0.001;
			System.out.println("Z algo time: " + intoSeconds + " seconds");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void getZarr(String str, int[] Z) {
		 
        int n = str.length();
        int L = 0, R = 0;
 
        for(int i = 1; i < n; ++i) {
 
            if(i > R){
 
                L = R = i;
 
 
                while(R < n && str.charAt(R - L) == str.charAt(R))
                    R++;
                 
                Z[i] = R - L;
                R--;
 
            }
            else{
                int k = i - L;
                if(Z[k] < R - i + 1)
                    Z[i] = Z[k];
                else{

                    L = i;
                    while(R < n && str.charAt(R - L) == str.charAt(R))
                        R++;
                    
                    Z[i] = R - L;
                    R--;
                }
            }
        }
    }
	public static void chapterOne() {
		File file = new File("");
		long start = System.currentTimeMillis();

		BufferedReader read;
		try {
			String currentPath = file.getAbsolutePath();
			read = new BufferedReader(new FileReader(currentPath + "/input.txt"));
			String line = read.readLine();

			int first = 0;
			String pattern = "";
			String result = "";
			String compiledString = "";
			
			while(line != null) {
				if(first == 0) {
					pattern = line;
					first++;
				}else {
					compiledString = compiledString + line;
				}
				
				line = read.readLine();
			}
			
			read.close();
			
			for(int i = 0; i < compiledString.length(); i++) {
				if(i + pattern.length()-1 >= compiledString.length()) {
					continue;
				}else {
					String temp = "";
					for(int j = 0; j < pattern.length(); j++) {
						temp = temp + compiledString.charAt(i+j);
					}
					
					for(int k = 0; k < temp.length(); k++) {
						if(temp.charAt(k) != pattern.charAt(k)) {
							break;
						}else {
							if(k == temp.length() -1) {
								result = result + String.valueOf(i) + " ";
							}else {
								continue;
							}
						}
						
					}
				}
			}
		
			
			
			result = result.trim();
			Path fName = Path.of(currentPath + "/output.txt");
			Files.writeString(fName, result);
			long end = System.currentTimeMillis();

			long elapsedTime = end - start;
			double intoSeconds = elapsedTime * 0.001;
			System.out.println("Chapter 1 time: " + intoSeconds + " seconds");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		 zAlgo();
		//chapterOne();		
	}

}
