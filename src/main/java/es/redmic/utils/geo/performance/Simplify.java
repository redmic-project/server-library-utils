package es.redmic.utils.geo.performance;

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

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;

public class Simplify {

	public static Geometry simplify(Geometry geomIn, int zoomLevel) {
		Double tolerance = 0.0;
		Geometry geom = geomIn;

		if (zoomLevel < 10) {
			tolerance = 0.01;
			if (zoomLevel < 6)
				tolerance = 0.1;

			geom = TopologyPreservingSimplifier.simplify(geomIn, tolerance);
		}

		return geom;
	}
}
