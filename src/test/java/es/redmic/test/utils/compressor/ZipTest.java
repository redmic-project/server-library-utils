package es.redmic.test.utils.compressor;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import es.redmic.exception.utils.DecompressException;
import es.redmic.utils.compressor.Zip;

public class ZipTest {

	Zip zip;

	String zipFile = "src/test/resources/data/compressor/test_file.zip",
			destinationDirectory = "src/test/resources/data/compressor/",
			tarFile = "src/test/resources/data/compressor/test_file.tar.gz";

	@Before
	public void setUp() {

		zip = new Zip();
	}

	@Test
	public void checkUnzipFile_IsSuccessful_WhenFileIsCorrect() {

		zip.extract(zipFile, destinationDirectory);

		File file = new File(destinationDirectory + "test_unzip");

		assertTrue(file.exists());
		file.delete();
	}

	@Test(expected = DecompressException.class)
	public void checkUnzipFile_ThrowDecompressException_IfFileNotFound() {

		zip.extract(zipFile + "fail", destinationDirectory);
	}

	@Test(expected = DecompressException.class)
	public void checkUnzipFile_ThrowDecompressException_IfFileIsNotZip() {

		zip.extract(tarFile, destinationDirectory);
	}
}
