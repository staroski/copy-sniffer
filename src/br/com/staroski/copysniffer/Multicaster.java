package br.com.staroski.copysniffer;

import java.io.File;

final class Multicaster implements SnifferListener {

	protected static SnifferListener add(SnifferListener toAdd, SnifferListener current) {
		if (toAdd == null) {
			return current;
		}
		if (current == null) {
			return toAdd;
		}
		return new Multicaster(toAdd, current);
	}

	protected static SnifferListener remove(SnifferListener toRemove, SnifferListener current) {
		if (toRemove == current || toRemove == null) {
			return null;
		} else if (toRemove instanceof Multicaster) {
			return ((Multicaster) toRemove).remove(current);
		} else {
			return toRemove;
		}
	}

	private final SnifferListener a;
	private final SnifferListener b;

	protected Multicaster(SnifferListener a, SnifferListener b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public void onFileCheckFinish(File file) {
		a.onFileCheckFinish(file);
		b.onFileCheckFinish(file);

	}

	@Override
	public void onFileCheckStart(File file) {
		a.onFileCheckStart(file);
		b.onFileCheckStart(file);

	}

	@Override
	public void onFolderScanFinish(File dir) {
		a.onFolderScanFinish(dir);
		b.onFolderScanFinish(dir);

	}

	@Override
	public void onFolderScanStart(File dir) {
		a.onFolderScanStart(dir);
		b.onFolderScanStart(dir);

	}

	@Override
	public void onPossibleCopyFound(File copy, Copies copies) {
		a.onPossibleCopyFound(copy, copies);
		b.onPossibleCopyFound(copy, copies);
	}

	private SnifferListener remove(SnifferListener current) {
		if (current == a) {
			return b;
		}
		if (current == b) {
			return a;
		}
		SnifferListener a2 = remove(a, current);
		SnifferListener b2 = remove(b, current);
		if (a2 == a && b2 == b) {
			return this;
		}
		return add(a2, b2);
	}
}