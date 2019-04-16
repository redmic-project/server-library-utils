package es.redmic.utils.compressor;

/*-
 * #%L
 * Utils
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
