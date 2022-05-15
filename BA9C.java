import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;


public class BA9C {
	
	
	public static class Node{
		String edge;
		ArrayList<Node> children;
		Node parent;
		
		public Node(String txt, Node parent) {
			this.edge = txt;
			this.parent = parent;
			children = new ArrayList<Node>();
		}
	}
	
	//Check if the current node has a child with the character c
	public static boolean containsChar(Node currentNode, char c) {
		boolean result = false;
		
		for(int i = 0; i < currentNode.children.size(); i++) {
			char currentChar = currentNode.children.get(i).edge.charAt(0);
			
			if(currentChar == c) {
				result = true;
			}
		}
		
		return result;
	}
	

	//Move the current node downwards towards the character c
	public static Node moveDown(Node currentNode, char c) {
		Node result = new Node("", null);
		
		for(int i = 0; i < currentNode.children.size(); i++) {
			char currentChar = currentNode.children.get(i).edge.charAt(0);
			
			if(currentChar == c) {
				result = currentNode.children.get(i);
			}
		}
		
		return result;
	}
	
    public static void main(String[] args) {
    	File file = new File("");   
    	ArrayList<String> suffixList = new ArrayList<String>();
        ArrayList<String> edgeList = new ArrayList<>();
        LinkedList<Node> queue = new LinkedList<>();
 		BufferedReader read;
		try {
			String currentPath = file.getAbsolutePath();
			read = new BufferedReader(new FileReader(currentPath + "/input.txt"));
			String line = read.readLine();
			String result = "";
			String compiledString = "";

			
			while(line != null) {
				compiledString = compiledString + line;
				line = read.readLine();
			}
			read.close();
			
			//Make the suffixes from the input text file
			for(int i = 0; i < compiledString.length(); i++) {
				suffixList.add(compiledString.substring(i, compiledString.length()));
			}
			
			//Create root / empty node
			Node root = new Node("", null);
			
			//For each suffix s
			for(String s : suffixList) {

			
				//Set current node to be the root
				Node currentNode = root;
				String edge = currentNode.edge;
				String matching = "";
				int mismatch = 0;
				int edgeCount = 0;
				boolean doNothing = false;
				boolean reset = false;
				boolean down = false;
				

				
				//Check if current node has a child starting with the first character in s
				if(containsChar(currentNode, s.charAt(0))) {
					//Move from root to the path with the correct first letter
					currentNode = moveDown(currentNode, s.charAt(0));
					edge = edge + currentNode.edge;
					edgeCount += currentNode.edge.length();
					
					//Iterate through the edge
					for(int i = 0; i < edge.length(); i++) {
						if(i == 1 && reset == true) {
							i = 0;
							reset = false;
						}
						
						//There was a mismatch, cancel the for loop
						if(edge.charAt(i) != s.charAt(i)) {
							mismatch = i;
							i = 999;
						}else {
							//No mismatch, keeping track of the string that matches
							matching = matching + edge.charAt(i);
						}
						
						//The edge ends before finishing checking the entirety of s
						if(i == edge.length()-1) {
							//Check if the next character in s exists within a child
							if(!containsChar(currentNode, s.charAt(i+1))) {
								//If not, it's a new character and can make a new child
								Node newNode = new Node(s.substring(i+1), currentNode);
								currentNode.children.add(newNode);
								doNothing = true;
							}else {
								//If it does, then move to that child and re-iterate everything
								currentNode = moveDown(currentNode, s.charAt(i+1));
								edge = edge + currentNode.edge;
								edgeCount += currentNode.edge.length();
								reset = true;
								i = 0;
								matching = "";
								down = true;
		
							}
						}
					}
					
					//Only do this process if nothing has been added to the tree this iteration
					if(!doNothing) {
						
						//Deals with weird edge cases
						if(mismatch > 1 && down) {
							edgeCount -= currentNode.edge.length();
							String test = currentNode.edge.substring(0, mismatch - edgeCount);
							matching = test;
						}
						//Make a new node in the same level as the current node, both sharing the same parent
						Node newNode = new Node(matching, currentNode.parent);
						currentNode.parent.children.add(newNode);
						//Make the current node a child of the new node
						newNode.children.add(currentNode);
						String edgeRemainder = edge.substring(mismatch);
						String suffixRemainder = s.substring(mismatch);
						//Change the current node's edge, removing the matching letters
						currentNode.edge = edgeRemainder;
						//Remove the current node from its parent
						currentNode.parent.children.remove(currentNode);
						//Make the parent of current node the new node to re-link it to the tree
						currentNode.parent = newNode;
						
						//add the remainder of the suffix to the new node
						Node suffixNode = new Node(suffixRemainder, newNode);
						newNode.children.add(suffixNode);
						
						if(edgeRemainder.equals("AC")) {
							System.out.println(s);
						}
					}
				 
				
				}else {
					//Current node doesn't have a child starting with the first character in s
					Node newNode = new Node(s, currentNode);
					currentNode.children.add(newNode);
				}
				
			}

			
			//Prints everything out
	        for(int i = 0; i < root.children.size(); ++i) {
	            queue.add(root.children.get(i));
	        }
	        while(!queue.isEmpty()) {
	            Node curr = queue.removeFirst();
	            for(int i = 0; i < curr.children.size(); ++i) {
	                queue.add(curr.children.get(i));
	            }
	            edgeList.add(curr.edge);
	        }
			
	        for(String str : edgeList) {
	        	result = result + str + "\n";
	        }
			result = result.trim();
			System.out.println(result);
			Path fName = Path.of(currentPath + "/output.txt");
			Files.writeString(fName, result);

		}catch(IOException e) {
			e.printStackTrace();
		}
    }
    
  
}
