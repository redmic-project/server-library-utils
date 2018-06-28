package es.redmic.test.utils.httpclient;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.exception.utils.ExternalResourceException;
import es.redmic.utils.httpclient.HttpClient;

public class HttpClientPostTest {

	HttpClient client;

	MockRestServiceServer mockServer;

	ObjectMapper objectMapper = new ObjectMapper();

	// @formatter:off

		String url = "http://redmic.net:8081/api/oauth/token";

	// @formatter:on

	@Before
	public void setUp() throws IOException {

		client = new HttpClient();

		mockServer = MockRestServiceServer.bindTo(client.getRestTemplate()).build();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void post_ReturnDTO_WhenRequestIsCorrect() throws Exception {

		mockServer.expect(requestTo(UriComponentsBuilder.fromHttpUrl(url).queryParams(getParams()).toUriString()))
				.andExpect(method(HttpMethod.POST))
				.andExpect(header("Authorization", getHeaders().get("Authorization")))
				.andExpect(queryParam("grant_type", "password")).andExpect(queryParam("username", "oag@redmic.es"))
				.andExpect(queryParam("password", "xxxx")).andExpect(queryParam("scope", "write"))
				.andRespond(withSuccess().body(objectMapper.writeValueAsString(getResponse()))
						.contentType(MediaType.APPLICATION_JSON));

		Map<String, String> result = (Map<String, String>) client.post(url, getParams(), getHeaders(),
				java.util.HashMap.class);

		assertEquals(result, getResponse());
	}

	@Test(expected = ExternalResourceException.class)
	public void post_ThrowException_WhenResourceNotFound() throws JsonProcessingException {

		mockServer.expect(requestTo(UriComponentsBuilder.fromHttpUrl(url).queryParams(getParams()).toUriString()))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));

		client.post(url, getParams(), getHeaders(), java.util.HashMap.class);
	}

	private MultiValueMap<String, String> getParams() {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("username", "oag@redmic.es");
		params.add("password", "xxxx");
		params.add("scope", "write");

		return params;
	}

	private Map<String, String> getHeaders() {

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Basic ZZBsOsNl3JldEtleQ==");

		return headers;
	}

	private Map<String, String> getResponse() {

		Map<String, String> response = new HashMap<>();

		response.put("access_token", "6c8dda0e-be28-49e5-9957-6c7e216fd9eb");
		response.put("token_type", "bearer");
		response.put("bearer", "528557");
		response.put("scope", "read write");

		return response;
	}
}
