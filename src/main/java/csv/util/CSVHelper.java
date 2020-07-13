//$Id$
package csv.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

import csv.writer.CSVWriter;
import utilities.Constants;
import utilities.FileUtil;
import utilities.JSONUtil;
import utilities.KeyHelper;

public class CSVHelper {

	// Writes the header to the fileWriter
	public static void writerHeader(CSVWriter csvWriter, Set<String> headerSet) {
		for (String header : headerSet) {
			csvWriter.writeTo(header);
		}
		csvWriter.endRecord();
	}

	public static void writerRecords(CSVWriter csvWriter, TreeSet<String> headerSet, List<JSONObject> jsonList) {
		for (JSONObject json : jsonList) {
			writeRecord(csvWriter, headerSet, json);
		}
	}

	private static void writeRecord(CSVWriter csvWriter, TreeSet<String> headerSet, JSONObject json) {
		for (String header : headerSet) {
			Object value = JSONUtil.getValue(json, header);

			// If it's a json object, we skip it
			if (value == null || value instanceof JSONObject) {
				csvWriter.writeTo(Constants.EMPTY_STRING);
			}

			// If it's a json array, we add to the global map, and write the PK
			// here
			else if (value instanceof JSONArray) {
				Long id = KeyHelper.getId(header);
				csvWriter.writeTo(id.toString());
				KeyHelper.addObject(header, id, (JSONArray) value);
			}

			else {
				String stringValue = value.toString();
				stringValue = stringValue.replace(",", Constants.EMPTY_STRING);
				csvWriter.writeTo(stringValue);
			}
		}
		csvWriter.endRecord();
	}

	public static void createCSVFilesForArray() {
		Set<String> arrayKeySet = KeyHelper.ARRA_KEY_VS_ID_VS_VALUE.keySet();

		for (String arrayKey : arrayKeySet) {

			HashMap<Long, JSONArray> idVsValue = KeyHelper.ARRA_KEY_VS_ID_VS_VALUE.get(arrayKey);

			String fileName = FileUtil.getOutputFileName(arrayKey);
			CSVWriter csvWriter = new CSVWriter(fileName);

			for (Long header : idVsValue.keySet()) {
				csvWriter.writeTo(header.toString());
				JSONArray jsonArray = idVsValue.get(header);
				Iterator<Object> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					csvWriter.writeTo(iterator.next().toString().replace(",", Constants.EMPTY_STRING));
				}
				csvWriter.endRecord();
			}
			csvWriter.close();
		}
	}
}
