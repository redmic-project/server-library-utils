package es.redmic.utils.httpclient;

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

import java.util.Collections;
import java.util.Map;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import es.redmic.exception.utils.ExternalResourceException;

public class HttpClient {

	RestTemplate restTemplate = new RestTemplate();

	public HttpClient() {

		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		restTemplate.setRequestFactory(requestFactory);
	}

	public Object get(String url, Class<?> templateClass) {

		ResponseEntity<?> response = null;

		try {
			response = restTemplate.exchange(url, HttpMethod.GET, null, templateClass);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response != null && response.getStatusCode().is2xxSuccessful())
			return response.getBody();

		throw new ExternalResourceException(url);
	}

	public Object post(String url, MultiValueMap<String, String> parameters, Map<String, String> headers,
			Class<?> templateClass) {

		ResponseEntity<?> response = null;

		HttpHeaders hd = new HttpHeaders();
		hd.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		hd.setContentType(MediaType.APPLICATION_JSON);

		headers.forEach((k, v) -> {
			hd.add(k, v);
		});

		HttpEntity<String> entity = new HttpEntity<>("parameters", hd);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(parameters);

		try {
			response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, templateClass);
		} catch (Exception e) {
		}

		if (response != null && response.getStatusCode().is2xxSuccessful())
			return response.getBody();

		throw new ExternalResourceException(url);
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

}
