package es.redmic.utils.geo.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.geotools.geojson.feature.FeatureJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.exception.mediastorage.MSFileNotFoundException;
import es.redmic.exception.utils.ProcessErrorException;
import es.redmic.jts4jackson.module.JTSModule;
import es.redmic.models.es.geojson.common.dto.GeoJSONFeatureCollectionDTO;

@Component
public class GeoUtils {

	FeatureJSON fjson = new FeatureJSON();

	ObjectMapper jacksonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.registerModule(new JTSModule());

	private final Logger LOGGER = LoggerFactory.getLogger(GeoUtils.class);

	public GeoUtils() {

	}

	public GeoJSONFeatureCollectionDTO convertShpToGeoJSON(File shapefile) {

		String folder = FilenameUtils.getFullPathNoEndSeparator(shapefile.getAbsolutePath());
		String sourceFile = shapefile.getAbsolutePath();
		String targetFile = folder + "/" + UUID.randomUUID().toString() + ".json";

		String[] cmd = { "bash", "-c",
				"ogr2ogr --config SHAPE_RESTORE_SHX yes -f \"GeoJSON\" " + targetFile + " " + sourceFile };

		Process p;
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			builder.redirectErrorStream(true);
			p = builder.start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {

			LOGGER.debug("Error ejecutando " + cmd[0]);
			throw new ProcessErrorException(e);
		}

		File geojsonFile = new File(targetFile);
		GeoJSONFeatureCollectionDTO collection = null;
		try {
			FileInputStream geoJson = new FileInputStream(targetFile);
			collection = jacksonMapper.readValue(geoJson, GeoJSONFeatureCollectionDTO.class);
		} catch (IOException e) {
			throw new MSFileNotFoundException(geojsonFile.getName(), folder, e);
		}

		return collection;
	}

}
