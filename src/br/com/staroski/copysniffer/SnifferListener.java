package br.com.staroski.copysniffer;

import java.io.File;

/**
 * @author Ricardo Artur Staroski
 */
public interface SnifferListener {

	public void onFileCheckFinish(File file);

	public void onFileCheckStart(File file);

	public void onFolderScanFinish(File dir);

	public void onFolderScanStart(File dir);

	public void onPossibleCopyFound(File copy, Copies copies);
}
