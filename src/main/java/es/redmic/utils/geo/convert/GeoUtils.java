package es.redmic.utils.geo.convert;

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
