package com.atcraft.NovelWebAccessor.web;

import java.net.URL;
import java.util.concurrent.Callable;

import com.atcraft.NovelWebAccessor.dataPack.NovChapter;

public class ChapterDownloadTask implements Callable<NovChapter>
{

	URL chapterUrl;
	/**
	 * 
	 * @param url 要下载的页面链接
	 */
	public ChapterDownloadTask(URL url)
	{
		chapterUrl=url;
	}
	public NovChapter call() throws Exception
	{
		return HtmlDecoder.extractChapter(HtmlCatcher.getDoc(chapterUrl));
	}

}
