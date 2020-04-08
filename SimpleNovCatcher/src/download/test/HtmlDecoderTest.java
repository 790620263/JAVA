package download.test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import org.junit.jupiter.api.Test;

import download.web.catcher.HtmlCatcher_http_unit;

class HtmlDecoderTest {

	@Test
	void testExtractChapter() {
		try {
			String url="https://www.123du.cc/dudu-32/74161/6459365.html";
//			new ChapterDownloadTask(new URL(url)).call();
//			HtmlDecoder.extractChapter(HtmlCatcher.getDoc(url));
//			HtmlDecoder.extractChapter(HtmlCatcher.getDoc("http://www.xbiquge.la/0/419/336636.html"));
			System.out.println(HtmlCatcher_http_unit.getDoc(new URL(url)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testGetTextNodes() {
		fail("Not yet implemented");
	}

	@Test
	void testExtractBookInfo() {
		fail("Not yet implemented");
	}

	@Test
	void testExtractChapterList_Remote() {
		fail("Not yet implemented");
	}

}
