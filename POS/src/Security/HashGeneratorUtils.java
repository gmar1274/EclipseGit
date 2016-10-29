package Security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGeneratorUtils {

	public static String generateSHA1(String message) {
		return hashString(message, "SHA-1");
	}

	public static String generateSHA256(String message) {
		return hashString(message, "SHA-256");
	}

	private static String hashString(String message, String algorithm) {

		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

			return convertByteArrayToHexString(hashedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String convertByteArrayToHexString(byte[] arrayBytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < arrayBytes.length; i++) {
			stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return stringBuffer.toString();
	}

	public static void main(String[] args) {
		try {
			String inputString = "acba";
			System.out.println("Input String: " + inputString);

			

			String sha1Hash = HashGeneratorUtils.generateSHA1(inputString);
			System.out.println("SHA-1 Hash: " + sha1Hash);

			//String a = HashGeneratorUtils.convertByteArrayToHexString(sha1Hash.getBytes()).;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}