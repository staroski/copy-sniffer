import java.io.File;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;

import br.com.staroski.copysniffer.Copies;
import br.com.staroski.copysniffer.Sniffer;
import br.com.staroski.copysniffer.SnifferListener;

/**
 * @author Ricardo Artur Staroski
 */
public final class Sniff implements SnifferListener {

	private static final Comparator<File> ASCENDING = new Comparator<File>() {

		@Override
		public int compare(File o1, File o2) {
			return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
		}
	};

	public static void main(String[] args) {
		try {
			if (args == null || args.length < 2) {
				System.out.println("usage:\njava " + Sniff.class.getName() + " <directories to scan> <result file>");
				return;
			}
			int count = args.length;
			String[] dirs = new String[count - 1];
			System.arraycopy(args, 0, dirs, 0, count - 1);

			File log = new File(args[count - 1]);

			Sniffer sniffer = new Sniffer();
			sniffer.addSniffListener(new Sniff());
			long time = System.currentTimeMillis();
			List<Copies> entries = sniffer.sniff(dirs);

			time = System.currentTimeMillis() - time;
			System.out.println("done in " + (time / (double) 1000) + " seconds!");
			System.out.println("writing file \"" + log.getAbsolutePath() + "\"...");
			log.getParentFile().mkdirs();
			try (PrintWriter out = new PrintWriter(log)) {
				for (Copies entry : entries) {
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

	private Sniff() {}

	@Override
	public void onFileCheckFinish(File file) {
		System.out.println("\"" + file.getAbsolutePath() + "\" checked!");
	}

	@Override
	public void onFileCheckStart(File file) {
		System.out.println("checking \"" + file.getAbsolutePath() + "\"...");
	}

	@Override
	public void onFolderScanFinish(File dir) {
		System.out.println("\"" + dir.getAbsolutePath() + "\" scanned!");
	}

	@Override
	public void onFolderScanStart(File dir) {
		System.out.println("scanning \"" + dir.getAbsolutePath() + "\"...");
	}

	@Override
	public void onPossibleCopyFound(File copy, Copies copies) {
		System.out.println("possible copy found \"" + copy.getAbsolutePath() + "\"");

	}
}
