package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

	public static void createFile(String dir, String contents) {
		try {
			File f = new File(dir);
			f.createNewFile();
			
			FileWriter w = new FileWriter(dir);
			w.write(contents);
			w.close();
		} catch (IOException e) {
			// File not created
			e.printStackTrace();
		}
		
	}
	
}
