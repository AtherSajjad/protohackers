package utils;

public class Validator {
	public static boolean isValidUserName(String userName) {
		if (userName.trim().isEmpty()) {
			return false;
		}

		return userName.matches("^[A-Za-z0-9]*$");
	}
}
