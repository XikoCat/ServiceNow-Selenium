package utils;

import java.util.Scanner;

public class DebuggingTool {
	public static void pause() {
        try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Program paused, press enter to resume");
			scanner.nextLine();
			System.out.println("Program resumed");
		}
    }
}
