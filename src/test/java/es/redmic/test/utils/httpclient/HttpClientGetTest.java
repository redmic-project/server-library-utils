package es.redmic.test.utils.httpclient;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.exception.utils.ExternalResourceException;
import es.redmic.utils.httpclient.HttpClient;

public class HttpClientGetTest {

	HttpClient client;

	MockRestServiceServer mockServer;

	ObjectMapper objectMapper = new ObjectMapper();

	// @formatter:off

		String aphia = "3",
			rank = "Kingdom",
			scientificname = "Plantae",
			classificationByAphia = "http://prueba.local/rest/AphiaClassificationByAphiaID/3";

	// @formatter:on

	DTO dtoResult;

	@Before
	public void setUp() throws IOException {

		client = new HttpClient();

		mockServer = MockRestServiceServer.bindTo(client.getRestTemplate()).build();

		dtoResult = new DTO(aphia, rank, scientificname);
	}

	@Test
	public void get_ReturnDTO_WhenRequestIsCorrect() throws Exception {

		mockServer.expect(requestTo(classificationByAphia)).andRespond(
				withSuccess().body(objectMapper.writeValueAsString(dtoResult)).contentType(MediaType.APPLICATION_JSON));

		DTO result = (DTO) client.get(classificationByAphia, DTO.class);

		assertEquals(result.getAphiaID(), aphia);
		assertEquals(result.getRank(), rank);
		assertEquals(result.getScientificname(), scientificname);
	}

	@Test(expected = ExternalResourceException.class)
	public void get_ThrowException_WhenResourceNotFound() throws JsonProcessingException {

		mockServer.expect(requestTo(classificationByAphia)).andRespond(withStatus(HttpStatus.NOT_FOUND));

		client.get(classificationByAphia, DTO.class);
	}

	public static class DTO {

		String AphiaID;

		String rank;

		String scientificname;

		public DTO() {
		}

		public DTO(String aphiaId, String rank, String scientificname) {

			setAphiaID(aphiaId);
			setRank(rank);
			setScientificname(scientificname);
		}

		public String getAphiaID() {
			return AphiaID;
		}

		public void setAphiaID(String aphiaID) {
			AphiaID = aphiaID;
		}

		public String getRank() {
			return rank;
		}

		public void setRank(String rank) {
			this.rank = rank;
		}

		public String getScientificname() {
			return scientificname;
		}

		public void setScientificname(String scientificname) {
			this.scientificname = scientificname;
		}
	}
}
