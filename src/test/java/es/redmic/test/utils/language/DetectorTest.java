package es.redmic.test.utils.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import es.redmic.utils.language.Detector;

public class DetectorTest {

	Detector detection;

	@Before
	public void setUp() throws IOException {

		detection = new Detector();
	}

	@Test
	public void testEN() throws IOException {

		assertEquals(detection.detectLanguage("The red house"), "en");

		assertEquals(detection.detectLanguage("The training text should be rather clean"), "en");

		assertEquals(detection.detectLanguageProbability("The red house"), "en");
	}

	@Test
	public void testNotEN() throws IOException {

		assertNotEquals(detection.detectLanguage("La maison rouge"), "en");

		assertNotEquals(detection.detectLanguage("El idioma español, o castellano, es una lengua romance"), "en");

		assertNotEquals(detection.detectLanguageProbability("La maison rouge"), "en");
	}

	@Test
	public void testES() throws IOException {

		assertEquals(detection.detectLanguage("Campaña de sedimentos Julio 2007."), "es");

		assertEquals(detection.detectLanguage("El mar Mediterráneo. Fauna, flora, ecología. I. Parte general"), "es");

		assertEquals(detection.detectLanguageProbability("Campaña de sedimentos Julio 2007"), "es");

		assertEquals(
				detection
						.detectLanguage("Galeommatacea e Cyamiacea. Parte II. Pp. 67-75. In: Fauna ibérica. Madrid: Museo Nacional de Ciencias Naturales. CSIC.  Pp."),
				"es");

		assertEquals(
				detection
						.detectLanguageProbability("Galeommatacea e Cyamiacea. Parte II. Pp. 67-75. In: Fauna ibérica. Madrid: Museo Nacional de Ciencias Naturales. CSIC.  Pp."),
				"es");

		assertEquals(
				detection
						.detectLanguageProbability("Eretmochelys imbricata. Pp. 67-75. In: Fauna ibérica. Madrid: Museo Nacional de Ciencias Naturales. CSIC.  Pp."),
				"es");

		assertEquals(
				detection
						.detectLanguage("Eretmochelys imbricata. Pp. 67-75. In: Fauna ibérica. Madrid: Museo Nacional de Ciencias Naturales. CSIC.  Pp."),
				"es");
	}

	@Test
	public void testNotES() throws IOException {

		assertNotEquals(detection.detectLanguage("The red house"), "es");

		assertNotEquals(detection.detectLanguage("The training text should be rather clean"), "es");

		assertNotEquals(detection.detectLanguageProbability("The red house"), "es");
	}

	@Test
	public void testFR() throws IOException {

		assertEquals(detection.detectLanguage("La maison rouge"), "fr");

		assertEquals(detection.detectLanguageProbability("La maison rouge"), "fr");
	}

	@Test
	public void testNotFR() throws IOException {

		assertNotEquals(detection.detectLanguage("The red house"), "fr");

		assertNotEquals(detection.detectLanguage("The training text should be rather clean"), "fr");

		assertNotEquals(detection.detectLanguageProbability("The red house"), "fr");
	}

	@Test
	public void testIT() throws IOException {

		assertEquals(
				detection
						.detectLanguage("Manzonia overdiepi, un nuovo gasteropodo marino (Rissoidae) delle isole Canarie e Madera"),
				"it");

		assertEquals(
				detection
						.detectLanguageProbability("Manzonia overdiepi, un nuovo gasteropodo marino (Rissoidae) delle isole Canarie e Madera"),
				"it");

		assertEquals(
				detection
						.detectLanguage("Erosaria spurca delle is. Canarie. La Conchiglia: International Shell Magazine, 19 (224-225): 5."),
				"it");

		assertEquals(
				detection
						.detectLanguageProbability("Erosaria spurca delle is. Canarie La Conchiglia: International Shell Magazine, 19 (224-225): 5."),
				"it");
	}

	@Test
	public void testStringEmpty() throws IOException {

		assertNull(detection.detectLanguage(""));
		assertNull(detection.detectLanguage("   "));
	}

	@Test
	public void testStringNull() throws IOException {

		assertNull(detection.detectLanguage(null));
	}

	@Test
	public void testStringEmptyProbability() throws IOException {

		assertNull(detection.detectLanguageProbability(""));
		assertNull(detection.detectLanguageProbability("   "));
	}

	@Test
	public void testStringNullProbability() throws IOException {

		assertNull(detection.detectLanguageProbability(null));
	}

	@Test
	public void testReturnProbability() throws IOException {

		boolean result = detection.detectLanguageReturnProbability("The training text should be rather clean") instanceof Double;
		assertTrue(result);
	}

	@Test
	public void testReturnProbabilityEmpty() throws IOException {

		assertNull(detection.detectLanguageReturnProbability(""));
		assertNull(detection.detectLanguageReturnProbability("  "));
	}

	@Test
	public void testReturnProbabilityNull() throws IOException {

		assertNull(detection.detectLanguageReturnProbability(null));
	}
}