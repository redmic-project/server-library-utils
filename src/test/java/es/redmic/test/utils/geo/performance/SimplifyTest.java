package es.redmic.test.utils.geo.performance;

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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import es.redmic.models.es.geojson.common.model.Feature;
import es.redmic.models.es.geojson.common.model.Properties;
import es.redmic.utils.geo.performance.Simplify;

public class SimplifyTest {

	private static final String FILENAME_LINESTRING_GEOJSON = "LineString_to_simplify.json";
	private ObjectMapper objectMapper = new ObjectMapper();
	private Feature<Properties, LineString> geojson;

	@Before
	public void init() throws IllegalArgumentException, IllegalAccessException, IOException {
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.registerModule(new JtsModule());

		InputStream file = getClass().getResource("/data/geo/geojson/" + FILENAME_LINESTRING_GEOJSON).openStream();

		TypeReference<Feature<Properties, LineString>> typeRef = new TypeReference<Feature<Properties, LineString>>() {
		};

		geojson = objectMapper.readValue(file, typeRef);
	}

	@Test
	public void should_returnLineStringWithLessAxes_when_simplifyAlgorithmFitZoom0()
			throws JsonParseException, JsonMappingException, IOException {

		Geometry geojsonDF = Simplify.simplify(geojson.getGeometry(), 0);

		assertTrue(geojson.getGeometry().getLength() > geojsonDF.getLength());
		assertTrue(geojson.getGeometry().getCoordinateSequence().size() > geojsonDF.getCoordinates().length);

	}

	@Test
	public void should_returnLineStringWithLessAxes_when_simplifyAlgorithmFitZoom5()
			throws JsonParseException, JsonMappingException, IOException {

		Geometry geojsonDF = Simplify.simplify(geojson.getGeometry(), 5);

		assertTrue(geojson.getGeometry().getLength() > geojsonDF.getLength());
		assertTrue(geojson.getGeometry().getCoordinateSequence().size() > geojsonDF.getCoordinates().length);

	}

	@Test
	public void should_returnSameLineString_when_simplifyAlgorithmFitZoomGreaterThan9()
			throws JsonParseException, JsonMappingException, IOException {

		Geometry geojsonDF = Simplify.simplify(geojson.getGeometry(), 10);

		assertTrue(geojson.getGeometry().getLength() == geojsonDF.getLength());
		assertTrue(geojson.getGeometry().getCoordinateSequence().size() == geojsonDF.getCoordinates().length);

	}

}
