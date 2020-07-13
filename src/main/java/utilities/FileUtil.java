//$Id$
package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileUtil {

	public static boolean isValidFile(String path) {
		return new File(path).exists();
	}

	public static boolean isJSONFile(String path) {
		return path.toLowerCase().endsWith(Constants.JSON_EXTENSION);
	}

	public static boolean isValidJSONFile(String path) {
		return isValidFile(path) && isJSONFile(path);
	}

	public static List<JSONObject> getListOfJSONs(String filePath) {

		boolean firstLine = true;
		boolean isJsonArray = false;

		StringBuilder strBuilder = new StringBuilder();
		ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			try {
				String line;
				while ((line = br.readLine()) != null) {

					// checking if the file content is a json Array
					if (firstLine) {
						firstLine = false;
						if (line.charAt(0) == Constants.JSON_ARRAY_START) {
							isJsonArray = true;
						}
					}

					// If its a jsonArray keep adding it to the stringBuilder
					if (isJsonArray) {
						strBuilder.append(line);
					}

					// If it's not a jsonArray, convert each line to jsonObject
					// and add it to the list
					else {
						JSONObject tempJson = JSONUtil.getJSONObject(line);
						if (tempJson != null) {
							jsonList.add(tempJson);
						}
					}
				}
			} finally {
				br.close();
			}
		} catch (IOException e) {
			Console.exitApp("IOException while reading from file");
		}

		// If it's a jsonArray, convert the strBuilder to jsonArray, and add the
		// json to the list
		if (isJsonArray) {
			JSONArray jsonArray = new JSONArray(strBuilder.toString());
			Iterator<Object> jsonIterator = jsonArray.iterator();
			while (jsonIterator.hasNext()) {
				JSONObject tempJson = (JSONObject) jsonIterator.next();
				if (tempJson != null) {
					jsonList.add(tempJson);
				}
			}
		}
		return jsonList;
	}

	public static String getOutputFileName() {
		return getOutputFileName(null);
	}

	public static String getOutputFileName(String key) {
		return key == null ? "csv_output.csv" : "csv_output_" + key.replaceAll("\\.", "_") + ".csv";
	}
}
