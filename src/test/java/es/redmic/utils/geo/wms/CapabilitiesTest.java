package es.redmic.utils.geo.wms;

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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.geotools.data.ows.Layer;
import org.geotools.ows.ServiceException;
import org.junit.Before;
import org.junit.Test;

import es.redmic.models.es.atlas.Contact;
import es.redmic.models.es.atlas.dto.LayerDTO;

public class CapabilitiesTest {

	GetCapabilities getCapabilities;

	LayerDTO layer;

	@Before
	public void setUp() throws ServiceException, MalformedURLException, IOException {

		String url = "src/test/resources/wms.xml";
		getCapabilities = new GetCapabilities(new File(url).toURI().toURL().toString());

		getCapabilities.getCapabilities();
	}

	@Test
	public void testWMSAtlasRedmic() throws IOException, ServiceException {

		assertEquals(getCapabilities.layers.size(), 5);

		assertEquals(getCapabilities.capabilities.getLayerList().size(), 6);
	}

	@Test
	public void testContact() throws IOException, ServiceException {

		Contact resultContact = getCapabilities.contact;

		assertEquals(resultContact.getName(), "José Andrés Sevilla");
		assertEquals(resultContact.getContactPosition(), "GIS");
		assertEquals(resultContact.getPhone(), "+34922298700");
		assertEquals(resultContact.getFax(), "+34922298704");
	}

	@Test
	public void testLayerData() throws IOException, ServiceException {

		Layer layerGet = getCapabilities.capabilities.getLayerList().get(3);

		LayerDTO layer = getCapabilities.layers.get(layerGet.getName());

		assertEquals(layer.getName(), layerGet.getName());
		assertEquals(layer.getTitle(), layerGet.getTitle());

		// System.out.println(layer.getLatLonBoundingBox().getMaxX());
		// System.out.println(layer.getBoundingBox());

		/*assertEquals(layer.getLatLonBoundingBox().getMaxX(), (Double) layerGet.getLatLonBoundingBox().getMaxX());
		assertEquals(layer.getLatLonBoundingBox().getMaxY(), (Double) layerGet.getLatLonBoundingBox().getMaxY());
		assertEquals(layer.getLatLonBoundingBox().getMinX(), (Double) layerGet.getLatLonBoundingBox().getMinX());
		assertEquals(layer.getLatLonBoundingBox().getMinY(), (Double) layerGet.getLatLonBoundingBox().getMinY());*/

		// assertEquals(layer.getStyleLayer().getName(),
		// layerGet.getStyles().get(0).getName());
		// assertEquals(layer.getStyleLayer().getUrl(),
		// layerGet.getStyles().get(0).getLegendURLs().get(0).toString());
	}

	@Test
	public void testLayerGrafCan() throws IOException, ServiceException {

		String url = "src/test/resources/OrtoExpress.xml";
		getCapabilities = new GetCapabilities(new File(url).toURI().toURL().toString());

		getCapabilities.getCapabilities();
	}

	@Test
	public void testParseAbstractLayerDefault() throws IOException, ServiceException {

		String abstractLayer = "Isolíneas batimétricas " + "\n(Batimetría de las Islas Canarias)\nref#817#";
		layer = new LayerDTO();
		layer.parseAbstractLayer(abstractLayer);

		assertEquals(layer.getAbstractLayer(), "Isolíneas batimétricas (Batimetría de las Islas Canarias) ");
		assertEquals(layer.getIdActivities().size(), 1);
		assertEquals(layer.getIdActivities().get(0), "817");

		abstractLayer = "Isolíneas batimétricas ref#817,201,54556# (Batimetría de las Islas Canarias)";
		layer = new LayerDTO();
		layer.parseAbstractLayer(abstractLayer);

		assertEquals(layer.getAbstractLayer(), "Isolíneas batimétricas (Batimetría de las Islas Canarias)");
		assertEquals(layer.getIdActivities().size(), 3);

		abstractLayer = "Isolíneas batimétricas " + "\n(Batimetría de las Islas Canarias)\nref#155,# aaaaaaaaaa";
		layer = new LayerDTO();
		layer.parseAbstractLayer(abstractLayer);

		assertEquals(layer.getAbstractLayer(), "Isolíneas batimétricas (Batimetría de las Islas Canarias) aaaaaaaaaa");
		assertEquals(layer.getIdActivities().size(), 1);
		assertEquals(layer.getIdActivities().get(0), "155");

		abstractLayer = "ref#155,#\nIsolíneas batimétricas (Batimetría de las Islas Canarias)\n";
		layer = new LayerDTO();
		layer.parseAbstractLayer(abstractLayer);

		assertEquals(layer.getAbstractLayer(), " Isolíneas batimétricas (Batimetría de las Islas Canarias) ");
		assertEquals(layer.getIdActivities().size(), 1);
		assertEquals(layer.getIdActivities().get(0), "155");

		abstractLayer = "ref#155#";
		layer = new LayerDTO();
		layer.parseAbstractLayer(abstractLayer);

		assertEquals(layer.getAbstractLayer(), "");
		assertEquals(layer.getIdActivities().size(), 1);
		assertEquals(layer.getIdActivities().get(0), "155");
	}
}
