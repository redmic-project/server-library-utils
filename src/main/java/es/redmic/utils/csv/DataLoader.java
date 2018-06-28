package es.redmic.utils.csv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

public class DataLoader {

	ICsvBeanReader beanReader;
	ICsvListReader listReader;
	String[] headers;

	public DataLoader() {
	}

	public DataLoader(String csvPath) {
		try {
			initial(csvPath);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void initial(String csvPath) throws UnsupportedEncodingException, FileNotFoundException {

		setBeanReader(new CsvBeanReader(new InputStreamReader(new FileInputStream(csvPath), "UTF-8"),
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE));

		setListReader(new CsvListReader(new InputStreamReader(new FileInputStream(csvPath), "UTF-8"),
				CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE));
	}

	public String[] header() throws Exception {

		try {
			setHeaders(getListReader().getHeader(true));

			if (getHeaders().length > 1)
				return getHeaders();
			else
				throw new Exception("Not headers in csv");

		} catch (Exception e) {
			throw e;

		} finally {
			// closeReaders();
		}
	}

	public ArrayList<String> groupBy(String column) throws Exception {

		ArrayList<String> groupBy = new ArrayList<String>();

		int numColumn = Arrays.asList(getHeaders()).indexOf(column);

		if (numColumn != -1) {
			String valueColumn = null;

			while (getListReader().read() != null) {
				valueColumn = getListReader().get(numColumn + 1);
				valueColumn = valueColumn.trim();

				if (groupBy.indexOf(valueColumn) == -1)
					groupBy.add(valueColumn);
			}

			return groupBy;
		} else
			throw new Exception("Not column in headers");
	}

	public void closeReaders() throws IOException {

		if (getBeanReader() != null) {
			getBeanReader().close();
		}
		if (getListReader() != null) {
			getListReader().close();
		}
	}

	public ICsvBeanReader getBeanReader() {
		return beanReader;
	}

	public void setBeanReader(ICsvBeanReader beanReader) {
		this.beanReader = beanReader;
	}

	public ICsvListReader getListReader() {
		return listReader;
	}

	public void setListReader(ICsvListReader listReader) {
		this.listReader = listReader;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

}