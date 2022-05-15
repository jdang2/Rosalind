import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;


public class BA9J {

	public static void main(String args[]) {
		
		File file = new File("");
		
		BufferedReader read;
		try {
			String currentPath = file.getAbsolutePath();
			String line = "ACTGCAG$TA";
			String compiledString = "ACTGCAG$TA";
			
	
			
			int strLen = compiledString.length();
			ArrayList<String> sortedList = new ArrayList<String>();
			ArrayList<String> originalList = new ArrayList<String>();
			LinkedHashMap<String, Integer> sortedHash = new LinkedHashMap<>();
			LinkedHashMap<String, Integer> originalHash = new LinkedHashMap<>();
			String [] sortedOccur = new String[strLen];
			Integer[] sortedPos = new Integer[strLen];
			String [] originalOccur= new String[strLen];
			Integer [] originalPos = new Integer[strLen];
			
			
			for(int i = 0; i < strLen; i++) {
				originalList.add(Character.toString(compiledString.charAt(i)));
			}
			for(int i = 0; i < strLen; i++) {
				sortedList.add(Character.toString(compiledString.charAt(i)));
			}
			Collections.sort(sortedList);
			
			
			
			for(int i = 0; i < strLen; i++) {
				String tempKey = sortedList.get(i);
				if(sortedHash.containsKey(tempKey)) {
					 sortedHash.put(tempKey, sortedHash.get(tempKey) + 1);
				}else {
					sortedHash.put(tempKey, 1);
				}
				
				sortedOccur[i] = tempKey;
				sortedPos[i] = sortedHash.get(tempKey);
				
				String otherKey = originalList.get(i);
				if(originalHash.containsKey(otherKey)) {
					originalHash.put(otherKey, originalHash.get(otherKey) + 1);
				}else {
					originalHash.put(otherKey, 1);
				}
				
				originalOccur[i] = otherKey;
				originalPos[i] = originalHash.get(otherKey);
				
			}
		
		
			String result = sortedList.get(0);
			int count = 0;
			for(int i = 0; i < strLen - 1; i++) {
				result = result + originalList.get(count);
				String lastLetter = Character.toString(result.charAt(result.length()-1));
				int pos = originalPos[count];
				count = changeIndex(lastLetter, pos, sortedOccur, sortedPos);
			}
			
			String reverse = "";
			for(int i = 0; i < result.length(); i++) {
				reverse = result.charAt(i) + reverse;
			}
			result = reverse;
			result = result.trim();
			Path fName = Path.of(currentPath + "/output");
			Files.writeString(fName, result);
			System.out.println(result);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static int changeIndex(String letter, int position, String [] sortedOccur, Integer [] sortedPos) {
		int result = 0;
		
		for(int i = 0; i < sortedOccur.length; i++) {
			if(sortedOccur[i].equals(letter) && sortedPos[i] == position) {
				result = i;
				break;
			}else {
				continue;
			}
		}
		
		return result;
	}
}
