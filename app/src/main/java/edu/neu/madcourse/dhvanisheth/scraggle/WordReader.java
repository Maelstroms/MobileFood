package edu.neu.madcourse.dhvanisheth.scraggle;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class WordReader {
	private static List<String> ls = null;
	private static Random random = new Random();
	
	public WordReader() throws Exception {
		//init();
	}
	
	public String getWord() {
		String ret = ls.get(random.nextInt(ls.size()));
		return ret;
	}
	
	public static void init(InputStream getjw) throws Exception {
		if(ls != null)
			return;
		
		ls = new ArrayList<String>();
		//File inFile = new File ("C:\\Users\\Dhvani Sheth\\AndroidStudioProjects\\NUMAD16S-DhvaniSheth\\app\\src\\main\\res\\raw\\nineletterwords.txt");
		Scanner sc = new Scanner (getjw);
		
		while (sc.hasNextLine())
	    {
	      String line = sc.nextLine();
	      ls.add(line);
	    }
		sc.close();
				
	}

}

	