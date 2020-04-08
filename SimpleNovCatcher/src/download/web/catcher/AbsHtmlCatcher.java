package download.web.catcher;

import java.net.URL;

import org.jsoup.nodes.Document;

interface AbsHtmlCatcher {

	public static String getHtml(String url) {
		return null;
	}
	public static Document getDoc(URL url)
	{
		return null;
	}
	public static String getHtml_do_js(String url) {
		return null;
	}
}
