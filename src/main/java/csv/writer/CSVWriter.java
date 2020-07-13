package csv.writer;

import java.io.FileWriter;
import java.io.IOException;

import utilities.Console;

public class CSVWriter {
	private static final char COLUMN_DELIMITER = ',';
	private static final char RECORD_DELIMITER = '\n';
	private static final char ESCAPE_CHARACTER = '/';

	private FileWriter fileWriter = null;

	private boolean first = true;

	public CSVWriter(String fileName) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName);
		} catch (IOException e) {
			Console.exitApp("Creating output file failed");
		}
		this.fileWriter = writer;
	}

	public void writeTo(String value) {
		try {
			appendColumnDelimiter();
			boolean quotesNeeded = isQuotesNeeded(value);
			if (quotesNeeded) {
				fileWriter.write('"');
			}

			for (int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				if (c == '"') {
					fileWriter.write(ESCAPE_CHARACTER);
				}
				fileWriter.write(c);
			}

			if (quotesNeeded) {
				fileWriter.write('"');
			}
		} catch (IOException exception) {
			Console.exitApp("IOException while writing to file");
		}
	}

	private void appendColumnDelimiter() throws IOException {
		if (first) {
			first = false;
		} else {
			fileWriter.write(COLUMN_DELIMITER);
		}
	}

	public void endRecord() {
		try {
			fileWriter.write(RECORD_DELIMITER);
			first = true;
		} catch (IOException exception) {
			Console.exitApp("IOException while writing to file");
		}
	}

	private boolean isQuotesNeeded(String value) {
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == COLUMN_DELIMITER || c == '"' || c == '\n' || c == '\r') {
				return true;
			}
		}
		return false;
	}

	public void close() {
		try {
			fileWriter.close();
		} catch (IOException exception) {
			Console.exitApp("IOException while writing to file");
		}
	}
}