package br.com.staroski.copysniffer;

import java.io.File;

/**
 * @author Ricardo Artur Staroski
 */
public abstract class SnifferListenerAdapter implements SnifferListener {

	@Override
	public void onFileCheckFinish(File file) {}

	@Override
	public void onFileCheckStart(File file) {}

	@Override
	public void onFolderScanFinish(File dir) {}

	@Override
	public void onFolderScanStart(File dir) {}

	@Override
	public void onPossibleCopyFound(File copy, Copies copies) {}
}
