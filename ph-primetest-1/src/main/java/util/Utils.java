package util;

import org.json.JSONObject;

public class Utils {
	public static boolean isValidRequest(String message) {
		boolean isValidJSON = isJSON(message);

		if (!isValidJSON) {
			return false;
		}
		JSONObject request = new JSONObject(message);
		boolean isValidMethod = isValidMethod(request);
		boolean isValidNumber = isValidNumber(request);

		return isValidJSON && isValidMethod && isValidNumber;

	}

	public static boolean isJSON(String message) {
		try {
			new JSONObject(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isValidMethod(JSONObject request) {
		try {
			if (!request.has("method")) {
				return false;
			}

			if (!(request.get("method") instanceof String)) {
				return false;
			}

			if (!request.getString("method").equals("isPrime")) {
				return false;
			}

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean isValidNumber(JSONObject request) {
		try {
			if (!request.has("number")) {
				return false;
			}

			if (!(request.get("number") instanceof Number)) {
				return false;
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
