package br.com.staroski.copysniffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CopySniffer {

	private static final MessageDigest SHA1;
	static {
		try {
			SHA1 = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private static String checksum(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			SHA1.reset();
			byte[] bytes = new byte[8192];
			for (int read = -1; (read = fis.read(bytes)) != -1; SHA1.update(bytes, 0, read)) {
			}
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

	public static void main(String[] args) {
		try {
			if (args == null || args.length < 2) {
				System.out.println("usage:\njava " + CopySniffer.class.getName() + " <result file> <directories to scan>");
				return;
			}
			new CopySniffer().sniff(args[0], args[1]);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private void scan(File dir, Map<String, List<File>> map) {
		File[] children = dir.listFiles();
		if (children == null) {
			return;
		}
		for (File child : children) {
			if (child.isDirectory()) {
				scan(child, map);
			} else {
				String checksum = checksum(child);
				if (checksum == null) {
					continue;
				}
				List<File> found = map.get(checksum);
				if (found == null) {
					found = new ArrayList<>();
					map.put(checksum, found);
				}
				found.add(child);
			}
		}

	}

	private static final Comparator<File> FILE_COMPARATOR = new Comparator<File>() {

		@Override
		public int compare(File o1, File o2) {
			return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
		}
	};

	public void sniff(File log, File... dirs) {
		Map<String, List<File>> map = new HashMap<>();
		long time = System.currentTimeMillis();
		for (File dir : dirs) {
			System.out.println("scanning \"" + dir.getAbsolutePath() + "\"...");
			scan(dir, map);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("done in " + (time / (double) 1000) + " seconds!");
		System.out.println("writing file \"" + log.getAbsolutePath() + "\"...");
		log.getParentFile().mkdirs();
		try (PrintWriter out = new PrintWriter(log)) {
			for (String checksum : map.keySet()) {
				List<File> files = map.get(checksum);
				if (files.size() > 1) {
					Collections.sort(files, FILE_COMPARATOR);
					out.println("possible copies {");
					for (File file : files) {
						out.println("\t" + file.getAbsolutePath());
					}
					out.println("}");
				}
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finished!");
	}

	public void sniff(String log, String dir) {
		sniff(new File(log), new File[] { new File(dir) });
	}
}
