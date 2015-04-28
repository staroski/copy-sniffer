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

	private void scan(File dir, Map<String, Entry> map) {
		listener.onFolderScanning(dir);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				scan(file, map);
			} else {
				listener.onFileChecking(file);
				String checksum = SHA1.checksum(file);
				listener.onFileChecked(file);
				if (checksum == null) {
					continue;
				}
				Entry entry = map.get(checksum);
				if (entry == null) {
					entry = new Entry();
					map.put(checksum, entry);
				}
				entry.add(file);
			}
		}
		listener.onFolderScanned(dir);
	}

	public List<Entry> sniff(File... dirs) {
		Map<String, Entry> map = new HashMap<>();
		for (File dir : dirs) {
			scan(dir, map);
		}
		final List<Entry> entries = new ArrayList<>();
		for (String checksum : map.keySet()) {
			Entry entry = map.get(checksum);
			if (entry.size() > 1) {
				entries.add(entry);
			}
		}
		return entries;
	}

	public List<Entry> sniff(String dir) {
		return sniff(new File[] { new File(dir) });
	}
}
