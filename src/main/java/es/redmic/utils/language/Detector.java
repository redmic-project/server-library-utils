package es.redmic.utils.language;

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
import java.util.List;

import com.google.common.base.Optional;
import com.optimaize.langdetect.DetectedLanguage;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

public class Detector {

	LanguageDetector lDetector;

	TextObjectFactory textObjectFactory;

	public Detector() throws IOException {

		// load all languages:
		List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

		// build language detector:
		lDetector = LanguageDetectorBuilder.create(NgramExtractors.standard()).withProfiles(languageProfiles).build();

		// create a text object factory
		textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
	}

	public String detectLanguage(String text) {

		if ((text != null) && (!text.isEmpty())) {
			TextObject textObject = getTextObjectFactory().forText(text);
			Optional<LdLocale> lang = getLDetector().detect(textObject);
			if (lang.isPresent())
				return lang.get().toString();
			else
				return detectLanguageProbability(text);
		} else
			return null;
	}

	public String detectLanguageProbability(String text) {

		if ((text != null) && (!text.isEmpty())) {

			TextObject textObject = getTextObjectFactory().forText(text);

			List<DetectedLanguage> containerResult = getLDetector().getProbabilities(textObject);

			if (!containerResult.isEmpty())
				return containerResult.get(0).getLocale().getLanguage().toString();
		}

		return null;
	}

	public Double detectLanguageReturnProbability(String text) {

		if ((text != null) && (!text.isEmpty())) {
			TextObject textObject = getTextObjectFactory().forText(text);

			List<DetectedLanguage> containerResult = getLDetector().getProbabilities(textObject);

			if (!containerResult.isEmpty())
				return containerResult.get(0).getProbability();
		}

		return (Double) null;
	}

	public LanguageDetector getLDetector() {
		return lDetector;
	}

	public void setLDetector(LanguageDetector languageDetector) {
		this.lDetector = languageDetector;
	}

	public TextObjectFactory getTextObjectFactory() {
		return textObjectFactory;
	}

	public void setTextObjectFactory(TextObjectFactory textObjectFactory) {
		this.textObjectFactory = textObjectFactory;
	}
}
