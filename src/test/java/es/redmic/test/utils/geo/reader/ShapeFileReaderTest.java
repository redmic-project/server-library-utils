package es.redmic.test.utils.geo.reader;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

import es.redmic.exception.utils.ShapeFileReaderException;
import es.redmic.utils.geo.reader.ShapeFileReader;

public class ShapeFileReaderTest {

	String shapeDir = "src/test/resources/data/geo/shapefile/";

	// Lee dos ficheros con un item cada uno
	@Test
	public void checkShapeFileReader_IsSuccessful_WhenDirectoryHas2ShapeFile() {

		ShapeFileReader reader = new ShapeFileReader(shapeDir);

		SimpleFeature item = reader.getNext();

		int countShape = 0;
		while (item != null) {

			countShape++;

			if (item.getID().contains("Point")) {
				assertEquals("GeometryTypeImpl Point<Point>", item.getDefaultGeometryProperty().getType().toString());
			}

			if (item.getID().contains("MultiLineString")) {
				assertEquals("GeometryTypeImpl MultiLineString<MultiLineString>",
						item.getDefaultGeometryProperty().getType().toString());
			}

			item = reader.getNext();
		}

		assertEquals(countShape, 2);
	}

	@Test
	public void checkGetHeader_ReturnSuccessful_WhenFileHasOneAttribute() {

		ShapeFileReader reader = new ShapeFileReader(shapeDir);

		List<String> header = reader.getHeader();

		assertEquals(header.size(), 1);
		// El tipo de geometría depende del fichero que procesa primero
		assertTrue(header.get(0).equals("Point") || header.get(0).equals("MultiLineString"));
	}

	@Test
	public void checkGetSampleWithSize10_ReturnOneSample_WhenFileHasOneRow() {

		ShapeFileReader reader = new ShapeFileReader(shapeDir);

		List<Map<String, String>> sample = reader.getSample(10);
		assertEquals(sample.size(), 2);
	}

	@Test(expected = ShapeFileReaderException.class)
	public void checkShapeFileReader_ThrowShapeFileReaderException_WhenDirectoryNotFound() {

		new ShapeFileReader(shapeDir + "fail/");
	}

	@Test(expected = ShapeFileReaderException.class)
	public void checkShapeFileReader_ThrowShapeFileReaderException_WhenDirectoryNotHasShapeFiles() {

		new ShapeFileReader(shapeDir + "emptyfolder/");
	}

	@Test(expected = ShapeFileReaderException.class)
	public void checkShapeFileReader_ThrowShapeFileReaderException_WhenDirectoryHasCorruptedShapeFile() {

		ShapeFileReader reader = new ShapeFileReader(shapeDir + "corruptedshapefile/");
		reader.getNext();
	}
}
