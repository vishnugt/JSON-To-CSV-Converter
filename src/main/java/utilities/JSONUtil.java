//$Id$
package utilities;

import static utilities.Constants.CSV_PARENT_CHILD_COLUMN_NAME_ADDER;
import static utilities.Constants.CSV_PARENT_CHILD_COLUMN_NAME_ADDER_ESCAPED;
import static utilities.Constants.EMPTY_STRING;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {

	// Returns JSON given input line
	public static JSONObject getJSONObject(String line) {
		try {
			JSONObject tempJson = new JSONObject(line);
			return tempJson;
		} catch (JSONException jsonException) {
			Console.print("Skipping invalid json - " + line.toString());
			return null;
		}
	}

	// Gets all the unique keys from the list of JSONs
	public static TreeSet<String> getUniqueKeys(List<JSONObject> jsonList) {
		TreeSet<String> uniqueKeys = new TreeSet<String>();
		for (JSONObject json : jsonList) {
			uniqueKeys.addAll(getUniqueKeys(json, null));
		}
		return uniqueKeys;
	}

	// Gets all the unique keys from the given json iteratively
	public static TreeSet<String> getUniqueKeys(JSONObject json, String parentName) {
		TreeSet<String> uniqueKeys = new TreeSet<String>();
		Iterator<String> keysIterator = json.keys();
		String parentFieldPrefix = parentName == null ? EMPTY_STRING : parentName + CSV_PARENT_CHILD_COLUMN_NAME_ADDER;
		while (keysIterator.hasNext()) {

			String key = keysIterator.next();
			Object value = json.get(key);
			String keyWithParentName = parentFieldPrefix + key;

			if (value instanceof JSONObject) {
				uniqueKeys.addAll(getUniqueKeys((JSONObject) value, keyWithParentName));
			}

			else {
				uniqueKeys.add(keyWithParentName);
			}
		}
		return uniqueKeys;
	}

	// Given a value like quiz.sports.q1, it gets the q1 after iteratively
	// reading form json
	public static Object getValue(JSONObject json, String header) {
		if (!header.contains(CSV_PARENT_CHILD_COLUMN_NAME_ADDER)) {
			return json.opt(header);
		}

		String innerKey = header.split(CSV_PARENT_CHILD_COLUMN_NAME_ADDER_ESCAPED)[0];
		JSONObject innerJson = json.getJSONObject(innerKey);
		return getValue(innerJson, header.replaceFirst(innerKey + CSV_PARENT_CHILD_COLUMN_NAME_ADDER, ""));
	}
}
