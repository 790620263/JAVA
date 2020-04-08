package com.atcraft.NovelWebAccessor.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlCatcher
{
	/**
	 * 产生访问网页的头文件
	 * @param url,用以获得host
	 * */
	protected static HashMap<String,String> getHeader(String url) throws MalformedURLException
	{
		HashMap<String, String> header = new HashMap<String, String>();
//		System.out.println(new URL(url).getHost());
//		header.put("Host", "http://www.baidu.com");
		
		header.put("Host", new URL(url).getHost());
		header.put("User-Agent", "  Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20170101 Firefox/5.0");
		header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		header.put("Accept-Language", "zh-cn,zh;q=0.5");
		header.put("Accept-Charset", "  GB2312,utf-8;q=0.7,*;q=0.7");
		header.put("Connection", "keep-alive");
		
		
		return header;
	}
	
	/**
	 * 获得网页的Document
	 * @param url
	 * @return jsoup.Document
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Document getDoc(String url) throws MalformedURLException, IOException
	{
		Connection connect = Jsoup.connect(url);
		Document doc = connect.headers(getHeader(url)).get();
		return doc;
	}
	public static Document getDoc(URL url) throws MalformedURLException, IOException
	{
		return getDoc(url.toString());		
	}
	
	
	
	/** 
	 * @param url
	 * @return 对应的HTML文档
	 * @throws IOException
	 * @throws URISyntaxException 网址错误
	 */
	public static String getHtml(String url) throws IOException, URISyntaxException
	{
//		System.out.println(doc.html());
		return getDoc(url).html();
	}
	public static String getHtml(URL url) throws IOException, URISyntaxException
	{
		return getDoc(url.toString()).html();
		
	}
	public static String getHtmlHead(String url) throws MalformedURLException, IOException
	{
		return getDoc(url).head().html();
	}
}
