package es.redmic.test.utils.geo.performance;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

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

		TypeReference<Feature<Properties, LineString>> typeRef = new TypeReference<Feature<Properties, LineString>>() {};

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
