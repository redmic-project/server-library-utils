package es.redmic.utils.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import es.redmic.exception.common.ExceptionType;
import es.redmic.exception.common.InternalException;
import es.redmic.exception.mediastorage.MSFileNotFoundException;

public class DataLoaderIngestData {

	String path;
	CsvPreference preference = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;
	CsvMapReader reader;
	String[] header;

	private final Logger LOGGER = LoggerFactory.getLogger(DataLoaderIngestData.class);

	public DataLoaderIngestData(File file, String delimiter) {

		CsvPreference csvPreferences = (delimiter != null
				? new CsvPreference.Builder('"', delimiter.charAt(0), "\n").build()
				: CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		this.preference = csvPreferences;

		openReader(file);
		readHeader();
	}

	private void openReader(File file) {
		try {
			reader = new CsvMapReader(new FileReader(file), preference);
		} catch (FileNotFoundException e) {
			throw new MSFileNotFoundException(file.getName(), path, e);
		}
	}

	private void readHeader() {
		if (reader.getRowNumber() == 0)
			try {
				header = reader.getHeader(true);
			} catch (IOException e) {
				LOGGER.debug("Error al leer el fichero");
				throw new InternalException(ExceptionType.INTERNAL_EXCEPTION, e);
			}
	}

	public List<String> getHeader() {
		return Arrays.asList(header);
	}

	public Map<String, String> read() {
		try {
			return reader.read(header);
		} catch (IOException e) {
			// TODO: crear excepcíon específica
			LOGGER.debug("Error al leer el fichero");
			throw new InternalException(ExceptionType.INTERNAL_EXCEPTION, e);
		}
	}

	public List<Map<String, String>> getSample(int totalRows) {
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		Map<String, String> row;
		int i = 0;

		try {
			while (((row = reader.read(header)) != null) && (i < totalRows)) {
				rows.add(row);
				i++;
			}
		} catch (IOException e) {
			// TODO: crear excepcíon específica
			LOGGER.debug("Error al leer el fichero");
			throw new InternalException(ExceptionType.INTERNAL_EXCEPTION, e);
		}

		return rows;
	}
}
