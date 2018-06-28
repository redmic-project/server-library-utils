package es.redmic.utils.geo.performance;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;


public class Simplify {
	
	public static Geometry simplify(Geometry geomIn, int zoomLevel){
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
