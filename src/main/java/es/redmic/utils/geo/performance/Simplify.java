package es.redmic.utils.geo.performance;

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
