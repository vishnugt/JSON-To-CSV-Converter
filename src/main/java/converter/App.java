package converter;

import java.util.List;
import java.util.TreeSet;

import org.json.JSONObject;

import csv.util.CSVHelper;
import csv.writer.CSVWriter;
import utilities.Console;
import utilities.FileUtil;
import utilities.JSONUtil;

/**
 * Driver class
 * 
 * Supports files that are both line separated JSONs and json array file<br>
 * Input file name should end with .json
 */
public class App {

	public static void main(String[] args) {
		Console.print("Initialising JSON to CSV converter");

		String filePath = null;

		if (args.length != 1) {
			Console.print("You can pass the file name as a argument or your can enter the file name now: ");
			filePath = Console.getFileName();
		}

		else {
			filePath = args[0];
		}

		if (!FileUtil.isValidJSONFile(filePath)) {
			Console.exitApp("Please give a valid location of a .json file");
		}

		// Will check if the file has a json Array or multiple json objects
		// separated by new line, and convert it to a list of json
		List<JSONObject> jsonList = FileUtil.getListOfJSONs(filePath);

		// Will return list of unique keys - if json is {"a":{"b":"c"},"d":"e"},
		// output will [a, a.b, d]
		TreeSet<String> uniqueJsonKeys = JSONUtil.getUniqueKeys(jsonList);

		CSVWriter csvWriter = new CSVWriter(FileUtil.getOutputFileName());

		// Writes the header to the file
		CSVHelper.writerHeader(csvWriter, uniqueJsonKeys);

		// Iterates the json and writes the values to the file
		CSVHelper.writerRecords(csvWriter, uniqueJsonKeys, jsonList);

		csvWriter.close();

		// This is used to create additional files for array types
		CSVHelper.createCSVFilesForArray();

		Console.printOutputFileLocation();

		Console.exitApp();
	}
}
