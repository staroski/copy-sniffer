package br.com.staroski.copysniffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Ricardo Artur Staroski
 */
final class SHA1 {

	private final MessageDigest SHA1;

	public SHA1() {
		try {
			SHA1 = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	public String checksum(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			return checksum(fis);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
			return null;
		}
	}

	public String checksum(InputStream input) {
		try {
			SHA1.reset();
			byte[] bytes = new byte[8192];
			for (int read = -1; (read = input.read(bytes)) != -1; SHA1.update(bytes, 0, read)) {}
			bytes = SHA1.digest();
			StringBuilder hexa = new StringBuilder();
			for (int i = 0, n = bytes.length; i < n; i++) {
				hexa.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			return hexa.toString();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
			return null;
		}
	}
}
