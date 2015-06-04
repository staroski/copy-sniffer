package br.com.staroski.copysniffer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ricardo Artur Staroski
 */
public final class Copies implements Iterable<File> {

	private final List<File> files = new ArrayList<>();

	Copies() {}

	public boolean add(File e) {
		return files.add(e);
	}

	public boolean isEmpty() {
		return files.isEmpty();
	}

	public Iterator<File> iterator() {
		return files.iterator();
	}

	public File remove(int index) {
		return files.remove(index);
	}

	public int size() {
		return files.size();
	}

	public void sort(Comparator<File> comparator) {
		Collections.sort(files, comparator);
	}
}
