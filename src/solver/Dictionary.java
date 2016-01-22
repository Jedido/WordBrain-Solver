package solver;

import java.util.Scanner;
import java.util.TreeSet;

public class Dictionary {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String line = in.nextLine();
		TreeSet<String> dictionary = new TreeSet<String>();
		while(!line.equals("quit")) {
			if(!hasNumber(line)) {
				if(line.contains(",")) {
					String[] words = line.split(", ");
					for(String s : words) {
						dictionary.add(s.toUpperCase());
					}
				} else if(line.contains("–")) {
					String[] words = line.split(" – ");
					for(String s : words) {
						dictionary.add(s.toUpperCase());
					}
				} else if(line.contains(" ")) {
					String[] words = line.split(" ");
					for(String s : words) {
						dictionary.add(s.toUpperCase());
					}
				} else {
					dictionary.add(line.toUpperCase());
				}
				
			}
			line = in.nextLine();
		}
		for(String s : dictionary) {
			System.out.println(s);
		}
		
		in.close();
	}
	
	public static boolean hasNumber(String s) {
		for(int i = 0; i < 10; i++) {
			if(s.contains("" + i)) return true;
		}
		return false;
	}
	
	public static void isWord(String s) {
		for(char c : s.toCharArray()) {
			if(c - 'A' < 0 && c - 'Z' > 0) {
				System.out.println(c + " not a letter for " + s);
			}
		}
	}
}
