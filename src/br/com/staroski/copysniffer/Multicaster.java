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
	public void onFileChecked(File file) {
		a.onFileChecked(file);
		b.onFileChecked(file);

	}

	@Override
	public void onFileChecking(File file) {
		a.onFileChecking(file);
		b.onFileChecking(file);

	}

	@Override
	public void onFolderScanned(File dir) {
		a.onFolderScanned(dir);
		b.onFolderScanned(dir);

	}

	@Override
	public void onFolderScanning(File dir) {
		a.onFolderScanning(dir);
		b.onFolderScanning(dir);

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
			return this; // it's not here
		}
		return add(a2, b2);
	}
}