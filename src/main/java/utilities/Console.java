//$Id$
package utilities;

import java.io.File;
import java.util.Scanner;

/**
 * Utility methods to print to console, and to exit app
 */
public class Console {

	public static void print(String message) {
		System.out.println(message);
	}

	public static void exitApp() {
		print("Exiting app");
		System.exit(0);
	}

	public static void exitApp(String reason) {
		print(reason + ". Hence quitting app!");
		System.exit(0);
	}

	public static String getFileName() {
		try {
			Scanner scanner = new Scanner(System.in);
			String filePath = scanner.nextLine();
			scanner.close();
			return filePath;
		} catch (Exception e) {
			exitApp("Invalid input");
			return null;
		}
	}

	public static void printOutputFileLocation() {
		Console.print("Output files has been created at " + new File(FileUtil.getOutputFileName()).getAbsolutePath());
	}
}
