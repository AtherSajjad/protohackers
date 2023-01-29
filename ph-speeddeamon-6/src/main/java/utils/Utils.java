package utils;

public class Utils {

	public static byte[] getLengthPrefixedString(String message) {
		int messageLength = message.length();

		byte[] messageBytes = new byte[messageLength + 1];
		messageBytes[0] = (byte) messageLength;
		for (int i = 0; i < messageLength; i++) {
			messageBytes[i + 1] = ((byte) message.charAt(i));
		}

		return messageBytes;

	}
}
