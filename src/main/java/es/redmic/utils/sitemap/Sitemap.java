package es.redmic.utils.sitemap;

import java.io.File;
import java.util.List;

import com.redfin.sitemapgenerator.MultipleLangSitemapGenerator;
import com.redfin.sitemapgenerator.MultipleLangSitemapUrl;

import es.redmic.exception.utils.SitemapGenerateException;

public class Sitemap {

	private MultipleLangSitemapGenerator wsg;

	private int urlCount = 0;

	private String baseUrl;

	private List<String> langs;

	private String defaultLang;

	public Sitemap(String baseUrl, String filePath, List<String> langs, String defaultLang) {

		this.langs = langs;

		this.defaultLang = defaultLang;

		this.baseUrl = baseUrl;
		
		try {
			wsg = MultipleLangSitemapGenerator.builder(baseUrl, new File(filePath))
					.allowMultipleSitemaps(true)
					.autoValidate(false)
					.build();
		} catch (Exception e) {
			throw new SitemapGenerateException(e);
		}
	}

	public void addUrl(String url) {

		try {

			MultipleLangSitemapUrl sitemapUrl = new MultipleLangSitemapUrl
					.Options(baseUrl + "/" + url, langs, defaultLang).build();

			wsg.addUrl(sitemapUrl);
			urlCount+=langs.size();
		} catch (Exception e) {
			throw new SitemapGenerateException(e);
		}
	}

	public void writeSitemap() {

		if (urlCount < MultipleLangSitemapGenerator.MAX_URLS_PER_SITEMAP)
			wsg.write();
		else
			wsg.writeSitemapsWithIndex();
	}

}
