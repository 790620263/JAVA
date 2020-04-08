package download;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import download.dataPack.NovChapter;
import download.web.catcher.HtmlCatcher;
import download.web.catcher.HtmlCatcher_http_unit;
import download.web.decoder.NovHtmlDecoder;




public class ChapterDownloadTask implements Callable<NovChapter>
{

	URL chapterUrl;
	boolean handleJS=true;
	/**
	 * 
	 * @param url 要下载的页面链接
	 */
	public ChapterDownloadTask(URL url)
	{
		this(url,false);
	}
	/**
	 * 
	 * @param url
	 * @param handleJS 是否处理JS，若处理则速度慢
	 */
	public ChapterDownloadTask(URL url,boolean handleJS)
	{
		chapterUrl=url;
		this.handleJS=handleJS;
	}
	public NovChapter call() throws MalformedURLException, IOException, IllegalAccessException 
	{
		NovChapter c=null;
		if(handleJS==false)
		{
			c=NovHtmlDecoder.extractChapter(HtmlCatcher.getDoc(chapterUrl));
		}else
		{
			c=NovHtmlDecoder.extractChapter(HtmlCatcher_http_unit.getDoc(chapterUrl));
		}
		return c;
	}

}
