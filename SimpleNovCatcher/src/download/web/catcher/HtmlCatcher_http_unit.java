package download.web.catcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 可以解析JS，但是速度比较慢
 * 
 * @author AtCraft
 *
 */
public class HtmlCatcher_http_unit implements AbsHtmlCatcher{

	protected static HashMap<String, String> header ;
	

	protected static String getPage(URL url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		if(header==null)
		{
			header= new HashMap<String, String>();
//			header.put("Host", new URL(url).getHost());
			header.put("User-Agent", "  Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20170101 Firefox/5.0");
			header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			header.put("Accept-Language", "zh-cn,zh;q=0.5");
			header.put("Accept-Charset", "  GB2312,utf-8;q=0.7,*;q=0.7");
			header.put("Connection", "keep-alive");
			
		}
		WebClient webClient=new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true  
	    webClient.getOptions().setCssEnabled(false); // 禁用css支持  
	    webClient.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常  
		
		WebRequest req = new WebRequest(url);
		req.setAdditionalHeaders(header);

		HtmlPage page = webClient.getPage(url);
		// 等待JS驱动dom完成获得还原后的网页
		webClient.waitForBackgroundJavaScript(2000);

		webClient.close();
		return page.asXml();
	}

	public static Document getDoc(URL chapterUrl)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String html = getPage(chapterUrl);
//		System.out.println(html);
		return Jsoup.parse(html);
	}
}
