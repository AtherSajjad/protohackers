package util;

import org.json.JSONObject;

public class Utils {

	public static boolean isJSON(String message) {
		try {
			new JSONObject(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isANumber(JSONObject request) {
		try {
			if (request.get("number") instanceof Number) {
				return true;
			}
		} catch (Exception e) {
		}

		return false;
	}

}
