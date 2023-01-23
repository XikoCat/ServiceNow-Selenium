package utils;

import java.util.ArrayList;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

	public class EmptyEnvFileException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}

	private static String PATH = "config.env";

	// Variables
	private String INSTANCE_ID;
	private boolean RUN_REMOTELY;
	private String REMOTE_SERVER_ADDRESS;
	private boolean EXIT_ON_FINISHED;

	public Config() throws EmptyEnvFileException {
		if (generate()) {
			System.out.println("Generated empty config.env");
			throw new EmptyEnvFileException();
		}

		Dotenv env = Dotenv.configure().filename(PATH).load();
		this.INSTANCE_ID = env.get("INSTANCE_ID");
		this.RUN_REMOTELY = env.get("RUN_REMOTELY").contains("true");
		this.REMOTE_SERVER_ADDRESS = env.get("REMOTE_SERVER_ADDRESS");
		this.EXIT_ON_FINISHED = env.get("EXIT_ON_FINISHED").contains("true");
	}

	public String getINSTANCE_ID() {
		return INSTANCE_ID;
	}

	public boolean getRUN_REMOTELY() {
		return RUN_REMOTELY;
	}

	public String getREMOTE_SERVER_ADDRESS() {
		return REMOTE_SERVER_ADDRESS;
	}

	public boolean getEXIT_ON_FINISHED() {
		return EXIT_ON_FINISHED;
	}

	private static boolean generate() {
		ArrayList<String[]> variables = new ArrayList<String[]>();

		// variables.add(new String[] { "NAME", "DEFAULT", "DESCRIPTION",
		// "ACCEPTED-VALUES"});
		variables.add(new String[] { "INSTANCE_ID", null, "Service Now Instance ID", "String" });
		variables.add(new String[] { "RUN_REMOTELY", "false", "Set to true to run the test on a remote machine",
				"true | false" });
		variables.add(new String[] { "REMOTE_SERVER_ADDRESS", "http://localhost:4444",
				"Remote machine address and port, make Selenium Standalone is running and port is open", "String" });
		variables.add(new String[] { "EXIT_ON_FINISHED", "true",
				"Whether to close the browser at the end of running the tests", "true | false" });

		return EnvHandler.genEnvFile(PATH, variables);
	}

}
