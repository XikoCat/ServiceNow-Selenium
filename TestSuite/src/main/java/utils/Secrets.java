package utils;

import java.util.ArrayList;

import io.github.cdimascio.dotenv.Dotenv;

public class Secrets {

	private static String PATH = "secrets.env";

	private String UTORID;
	private String UTORPASS;

	public Secrets() {
		if (generate()) {
			System.out.println("Generated empty secrets.env");
			return;
		}

		Dotenv env = Dotenv.configure().filename(PATH).load();
		this.UTORID = env.get("UTORID");
		this.UTORPASS = env.get("UTORPASS");
	}

	public String getUTORID() {
		return UTORID;
	}

	public String getUTORPASS() {
		return UTORPASS;
	}

	private static boolean generate() {
		ArrayList<String[]> variables = new ArrayList<String[]>();

		// variables.add(new String[] { "NAME", "DEFAULT",
		// "DESCRIPTION","ACCEPTED-VALUES"});
		variables.add(new String[] { "UTORID", null, "University of Toronto UTORID", "String" });
		variables.add(new String[] { "UTORPASS", null, "University of Toronto UTORID Password", "String" });
		return EnvHandler.genEnvFile(PATH, variables);
	}

}
