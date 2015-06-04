package br.com.staroski.copysniffer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ricardo Artur Staroski
 */
public final class Sniffer {

	private SnifferListener listener = new SnifferListenerAdapter() {};

	private final SHA1 SHA1 = new SHA1();

	public void addSniffListener(SnifferListener listener) {
		this.listener = Multicaster.add(listener, this.listener);
	}

	public void removeSniffListener(SnifferListener listener) {
		this.listener = Multicaster.remove(listener, this.listener);
	}

	private void scan(File dir, Map<String, Copies> map) {
		listener.onFolderScanStart(dir);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				scan(file, map);
			} else {
				listener.onFileCheckStart(file);
				String checksum = SHA1.checksum(file);
				listener.onFileCheckFinish(file);
				if (checksum == null) {
					continue;
				}
				Copies copies = map.get(checksum);
				if (copies == null) {
					copies = new Copies();
					map.put(checksum, copies);
				}
				copies.add(file);
				if (copies.size() > 1) {
					listener.onPossibleCopyFound(file, copies);
				}
			}
		}
		listener.onFolderScanFinish(dir);
	}

	public List<Copies> sniff(File... dirs) {
		Map<String, Copies> map = new HashMap<>();
		for (File dir : dirs) {
			scan(dir, map);
		}
		final List<Copies> entries = new ArrayList<>();
		for (String checksum : map.keySet()) {
			Copies entry = map.get(checksum);
			if (entry.size() > 1) {
				entries.add(entry);
			}
		}
		return entries;
	}

	public List<Copies> sniff(String... dirs) {
		int count = dirs.length;
		File[] folders = new File[count];
		for (int i = 0; i < count; i++) {
			folders[i] = new File(dirs[i]);
		}
		return sniff(folders);
	}
}
