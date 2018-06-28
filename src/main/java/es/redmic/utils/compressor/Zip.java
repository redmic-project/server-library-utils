package es.redmic.utils.compressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import es.redmic.exception.utils.DecompressException;

public class Zip {

	public Zip() {
	}

	@SuppressWarnings("resource")
	public void extract(String file, String destinationDirectory) {

		byte[] buffer = new byte[1024];

		try {

			createDirectory(destinationDirectory);

			ZipInputStream zis = new ZipInputStream(new FileInputStream(file));

			ZipEntry zipEntry = zis.getNextEntry();

			if (zipEntry == null) {
				throw new DecompressException();
			}

			while (zipEntry != null) {
				String fileName = zipEntry.getName();
				File newFile = new File(destinationDirectory + fileName);
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (IOException e) {
			throw new DecompressException(e);
		}
	}

	private void createDirectory(String path) {

		File dir = new File(path);

		if (!dir.exists()) {
			try {
				dir.mkdir();
			} catch (SecurityException e) {
				throw new DecompressException(e);
			}
		}
	}
}
