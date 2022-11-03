package utils;

public class Validator {
	public static boolean isValidUserName(String userName) {
		if (userName.trim().isEmpty()) {
			return false;
		}
		for (char ch : userName.toCharArray()) {
			if (!Character.isAlphabetic(ch)) {
				return false;
			}
		}
		return true;
	}
}
