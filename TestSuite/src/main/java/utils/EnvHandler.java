package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class EnvHandler {

	private static void createFile(String fileName) {
		try {
			File fileObj = new File(fileName);
			if (fileObj.createNewFile()) {
				System.out.println("File created: " + fileObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred while creating a file");
			e.printStackTrace();
		}
	}

	private static void writeToFile(String fileName, ArrayList<String[]> variables) {
		try {
			FileWriter writerObj = new FileWriter(fileName);
			for (int i = 0; i < variables.size(); i++) {
				String[] var = variables.get(i);
				// {"NAME", "DEFAULT", "DESCRIPTION", "ACCEPTED-VALUES"}
				String name = var[0];
				String deft = var[1];
				String desc = var[2];
				String accp = var[3];

				if (desc != null)
					writerObj.write("# " + desc + "\n");

				if (accp != null || deft != null)
					writerObj.write("# ");
				if (accp != null)
					writerObj.write("[" + accp + "]");
				if (accp != null && deft != null)
					writerObj.write(" | ");
				if (deft != null)
					writerObj.write("Default: "+deft);
				if (accp != null || deft != null)
					writerObj.write("\n");

				writerObj.write(name + "=");
				if (deft != null)
					writerObj.write(deft);
				writerObj.write("\n\n");
			}
			writerObj.close();
			System.out.println("Successfully wrote to: " + fileName);
		} catch (IOException e) {
			System.out.println("An error occurred writing to: " + fileName);
			e.printStackTrace();
		}
	}

	public static boolean genEnvFile(String fileName, ArrayList<String[]> variables) {
		try {
			Dotenv.configure().filename(fileName).load();
			return false;
		} catch (DotenvException e) {
			// File could not be loaded -> Create file template
			createFile(fileName);
			writeToFile(fileName, variables);
			return true;
		}
	}

}
