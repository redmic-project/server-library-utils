package es.redmic.test.utils.geo.convert;

import static org.junit.Assert.assertNotNull;

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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.support.membermodification.MemberModifier;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.models.es.geojson.GeoJSONFeatureType;
import es.redmic.models.es.geojson.common.dto.GeoJSONFeatureCollectionDTO;
import es.redmic.utils.geo.convert.GeoUtils;


public class ConvertShapeFileToGeoJsonTest {
	static final String FILENAME_POINT_SHP = "Point.shp";
	static final String FILENAME_LINESTRING_SHP = "LineString.shp";

	@InjectMocks
	GeoUtils geoUtils;

	@Before
	public void init() throws IllegalArgumentException, IllegalAccessException {
		ObjectMapper jacksonMapper = Mockito.spy(new ObjectMapper());
		//jacksonMapper.registerModule(new JtsModule());
		jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		MockitoAnnotations.initMocks(this);
		MemberModifier.field(GeoUtils.class, "jacksonMapper").set(geoUtils, jacksonMapper);
	}


	@SuppressWarnings("unchecked")
	@Test
	public void convertPointShapeFileToGeoJson() throws URISyntaxException {
		URL resource = getClass().getResource("/data/geo/shapefile/" + FILENAME_POINT_SHP);
		File file = Paths.get(resource.toURI()).toFile();

		GeoJSONFeatureCollectionDTO geojson = geoUtils.convertShpToGeoJSON(file);
		assertNotNull(geojson);
		assertTrue(geojson.getFeatures().size() == 1);
		assertTrue(geojson.getFeatures().get(0) instanceof HashMap);

		HashMap<String, Object> feature = (HashMap<String, Object>) geojson.getFeatures().get(0);
		assertTrue(feature.get("type").toString().equals(GeoJSONFeatureType.Constants.FEATURE));
		assertTrue(feature.get("geometry").toString().contains("Point"));
//		assertTrue(feature.getGeometry().getGeometryType().equals(GeoJSONObjectTypes.Constants.POINT));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void convertLineStringShapeFileToGeoJson() throws URISyntaxException {

		URL resource = getClass().getResource("/data/geo/shapefile/" + FILENAME_LINESTRING_SHP);
		File file = Paths.get(resource.toURI()).toFile();

		GeoJSONFeatureCollectionDTO geojson = geoUtils.convertShpToGeoJSON(file);
		assertNotNull(geojson);
		assertTrue(geojson.getFeatures().size() == 1);
		assertTrue(geojson.getFeatures().get(0) instanceof HashMap);

		HashMap<String, Object> feature = (HashMap<String, Object>) geojson.getFeatures().get(0);
		assertTrue(feature.get("type").toString().equals(GeoJSONFeatureType.Constants.FEATURE));
		assertTrue(feature.get("geometry").toString().contains("LineString"));

	}
}
