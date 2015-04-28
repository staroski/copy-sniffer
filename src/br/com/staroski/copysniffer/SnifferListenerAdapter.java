package br.com.staroski.copysniffer;

import java.io.File;

/**
 * @author Ricardo Artur Staroski
 */
public abstract class SnifferListenerAdapter implements SnifferListener {

	@Override
	public void onFileChecked(File file) {}

	@Override
	public void onFileChecking(File file) {}

	@Override
	public void onFolderScanned(File dir) {}

	@Override
	public void onFolderScanning(File dir) {}

}
