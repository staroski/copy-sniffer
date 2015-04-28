package br.com.staroski.copysniffer;

import java.io.File;

/**
 * @author Ricardo Artur Staroski
 */
public interface SnifferListener {

	public void onFileChecked(File file);

	public void onFileChecking(File file);

	public void onFolderScanned(File dir);

	public void onFolderScanning(File dir);
}
