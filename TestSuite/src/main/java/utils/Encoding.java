package utils;

public class Encoding {

	public static String[][] ENCODED_QUERY_TO_URL = { { "=", "%253D" }, { "^", "%255E" } };

	public static String encode(String input, String[][] code) {
		for (int i = 0; i < code.length; i++) {
			String[] temp = input.split(code[i][0]);
			String.join(code[i][1], temp);
		}
		return input;
	}

}
