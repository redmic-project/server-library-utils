package es.redmic.test.utils.csv;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import es.redmic.utils.csv.DataLoader;

public class DataLoaderTest {

	DataLoader dataLoader;

	@Before
	public void setUp() throws IOException {

		dataLoader = new DataLoader();
	}

	@Test
	public void testHeader() throws Exception {

		String csvPath = "src/test/resources/pCSV.csv";
		dataLoader.initial(csvPath);
		String[] headerCSV = dataLoader.header();

		assertEquals(12, headerCSV.length);
	}

	@Test
	public void testNotHeader() throws IOException {

		String csvPath = "src/test/resources/prueba.csv";
		dataLoader.initial(csvPath);
		try {
			dataLoader.header();
			fail("Not excepcion");
		} catch (Exception e) {
			assertThat(e, instanceOf(Exception.class));
		}
	}

	@Test
	public void testGroupByNotColumn() throws Exception {

		String column = "Clase de la que no hay";
		try {
			dataLoader.groupBy(column);
			fail("Not excepcion");
		} catch (Exception e) {
			assertThat(e, instanceOf(Exception.class));
		}
	}

}