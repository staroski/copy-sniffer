package br.com.staroski.copysniffer;

import java.io.File;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;

/**
 * @author Ricardo Artur Staroski
 */
public final class Run implements SnifferListener {

	private static final Comparator<File> ASCENDING = new Comparator<File>() {

		@Override
		public int compare(File o1, File o2) {
			return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
		}
	};

	public static void main(String[] args) {
		try {
			if (args == null || args.length < 2) {
				System.out.println("usage:\njava " + Run.class.getName() + " <directories to scan> <result file>");
				return;
			}
			Sniffer sniffer = new Sniffer();
			sniffer.addSniffListener(new Run());
			long time = System.currentTimeMillis();
			List<Entry> entries = sniffer.sniff(args[0]);

			time = System.currentTimeMillis() - time;
			System.out.println("done in " + (time / (double) 1000) + " seconds!");
			File log = new File(args[1]);
			System.out.println("writing file \"" + log.getAbsolutePath() + "\"...");
			log.getParentFile().mkdirs();
			try (PrintWriter out = new PrintWriter(log)) {
				for (Entry entry : entries) {
					if (entry.size() > 1) {
						entry.sort(ASCENDING);
						out.println("possible copies {");
						for (File file : entry) {
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
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private Run() {}

	@Override
	public void onFileChecked(File file) {
		System.out.println("\"" + file.getAbsolutePath() + "\" checked!");
	}

	@Override
	public void onFileChecking(File file) {
		System.out.println("checking \"" + file.getAbsolutePath() + "\"...");
	}

	@Override
	public void onFolderScanned(File dir) {
		System.out.println("\"" + dir.getAbsolutePath() + "\" scanned!");
	}

	@Override
	public void onFolderScanning(File dir) {
		System.out.println("scanning \"" + dir.getAbsolutePath() + "\"...");
	}
}
