package es.redmic.utils.geo.reader;

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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import es.redmic.exception.mediastorage.MSFileNotFoundException;
import es.redmic.exception.utils.ShapeFileReaderException;

public class ShapeFileReader {

	private List<File> files;

	private FeatureIterator<SimpleFeature> features;

	private Iterator<File> filesIterator;

	private Map<String, Object> map = new HashMap<>();

	private final String shapeFileExtension = ".shp";

	private SimpleFeatureType schema;

	private List<String> header = new ArrayList<>();

	public ShapeFileReader(String tempDir) {

		initialize(tempDir);
	}

	/*
	 * Dado un directorio donde están almacenados los shapefiles a leer se
	 * procesan todos los ficheros con extensión .shp de dicho directorio
	 * 
	 * TODO: Implementar si es necesario, la posibilidad de pasale un solo
	 * fichero.
	 * 
	 */
	private void initialize(String tempDir) {

		File folder = new File(tempDir);

		if (!folder.isDirectory())
			throw new ShapeFileReaderException(new MSFileNotFoundException(tempDir));

		files = Arrays.asList(folder.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {

				return name.toLowerCase().endsWith(shapeFileExtension);
			}
		}));

		if (files == null || files.size() < 1)
			throw new ShapeFileReaderException(new MSFileNotFoundException(tempDir));

		map.put("enable spatial index", false);
		map.put("create spatial index", false);

		initializeFileIterator();

		loadNewFile();
		createDescriptionFile();
	}

	private void initializeFileIterator() {

		filesIterator = files.iterator();
	}

	private Boolean hasNext() {

		return ((features != null && features.hasNext()) || loadNewFile());
	}

	public SimpleFeature getNext() {

		if (!hasNext()) {
			return null;
		}
		return features.next();
	}

	public List<String> getHeader() {

		return header;
	}

	public List<Map<String, String>> getSample(int size) {

		List<Map<String, String>> data = new ArrayList<>();

		int count = 1;

		SimpleFeature item = getNext();

		while (item != null || count == size) {

			Map<String, String> itemSample = new HashMap<>();

			for (Property attr : item.getProperties()) {

				if (!attr.getName().toString().equals("the_geom"))
					itemSample.put(attr.getName().toString(), attr.getValue().toString());
			}

			data.add(itemSample);

			item = getNext();

			count++;

			if (item == null)
				break;
		}

		initializeFileIterator();

		return data;
	}

	private boolean loadNewFile() {

		if (!filesIterator.hasNext())
			return false;

		File file = filesIterator.next();

		try {

			map.put("url", file.toURI().toURL());

			DataStore dataStore = DataStoreFinder.getDataStore(map);
			String typeName = dataStore.getTypeNames()[0];

			FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
			Filter filter = Filter.INCLUDE;

			FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);

			schema = source.getSchema();

			features = collection.features();

		} catch (Exception e) {
			throw new ShapeFileReaderException(e);
		}
		return true;
	}

	private void createDescriptionFile() {

		if (schema == null)
			return;

		header.clear();

		for (int i = 0; i < schema.getAttributeCount(); i++) {
			header.add(schema.getType(i).getName().toString());
		}
	}
}
