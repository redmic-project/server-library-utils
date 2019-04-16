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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;

import es.redmic.models.es.atlas.Contact;
import es.redmic.models.es.atlas.dto.LayerDTO;

public class GetCapabilities {

	WMSCapabilities capabilities;
	Contact contact;
	HashMap<String, LayerDTO> layers;
	String urlSource;

	public GetCapabilities(String url) throws ServiceException, IOException {

		URL serverURL = new URL(url);

		if (serverURL.getQuery() != null)
			serverURL = new URL(url.replaceAll(serverURL.getQuery(), ""));

		setUrlSource(url);

		WebMapServer wms = new WebMapServer(serverURL);
		capabilities = wms.getCapabilities();
	}

	public HashMap<String, LayerDTO> getCapabilities() throws ServiceException, IOException {

		if (capabilities.getService().getContactInformation() != null)
			setContact(new Contact(capabilities.getService().getContactInformation()));

		setLayers(new HashMap<String, LayerDTO>());

		layerList(capabilities.getLayerList());

		return getLayers();
	}

	public void layerList(List<Layer> layerList) {
		for (int i = 0; i < layerList.size(); i++) {
			LayerDTO layerAux;
			if (layerList.get(i).getName() != null) {
				layerAux = new LayerDTO(layerList.get(i));
				layerAux.setContact(getContact());
				layerAux.setUrlSource(getUrlSource());
				layerAux.setFormats(capabilities.getRequest().getGetMap().getFormats());
				getLayers().put(layerAux.getName(), layerAux);
			}
		}
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public HashMap<String, LayerDTO> getLayers() {
		return layers;
	}

	public void setLayers(HashMap<String, LayerDTO> layers) {
		this.layers = layers;
	}

	public void setCapabilities(WMSCapabilities capabilities) {
		this.capabilities = capabilities;
	}

	public String getUrlSource() {
		return urlSource;
	}

	public void setUrlSource(String urlSource) {
		this.urlSource = urlSource;
	}
}
