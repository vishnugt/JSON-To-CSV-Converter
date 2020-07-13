//$Id$
package utilities;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;

public class KeyHelper {

	// Used to maintain PK for array type values
	private static final HashMap<String, AtomicLong> ARRAY_KEY_VS_ID = new HashMap<String, AtomicLong>();

	// Used to maintain array_key vs id vs jsonarray values
	public static final HashMap<String, HashMap<Long, JSONArray>> ARRA_KEY_VS_ID_VS_VALUE = new HashMap<String, HashMap<Long, JSONArray>>();

	public static Long getId(String arrayKey) {
		if (!ARRAY_KEY_VS_ID.containsKey(arrayKey)) {
			ARRAY_KEY_VS_ID.put(arrayKey, new AtomicLong(0l));
		}
		return ARRAY_KEY_VS_ID.get(arrayKey).incrementAndGet();
	}

	public static void addObject(String arrayKey, Long id, JSONArray value) {
		if (!ARRA_KEY_VS_ID_VS_VALUE.containsKey(arrayKey)) {
			ARRA_KEY_VS_ID_VS_VALUE.put(arrayKey, new HashMap<Long, JSONArray>());
		}

		HashMap<Long, JSONArray> idVsValue = ARRA_KEY_VS_ID_VS_VALUE.get(arrayKey);
		idVsValue.put(id, value);
	}
}
