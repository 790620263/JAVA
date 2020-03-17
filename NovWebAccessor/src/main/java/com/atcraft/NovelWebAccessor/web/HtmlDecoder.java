package com.atcraft.NovelWebAccessor.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.atcraft.NovelWebAccessor.dataPack.NovChapter;
import com.atcraft.NovelWebAccessor.dataPack.NovInfo;

import tool.CharsetDectector;
/**
 * 用以解析Html
 * @author AtCraft
 *
 */
public class HtmlDecoder
{
	/*jousp的一些说明
	 * 摘自：https://www.ibm.com/developerworks/cn/java/j-lo-jsouphtml/
	 * 
	 * 
//假设我们获取的HTML的字符内容如下
String html = "<html><div id=\"blog_list\"><div class=\"blog_title\"><a href=\"url1\">第一篇博客</a></div><div class=\"blog_title\"><a href=\"url2\">第二篇博客</a></div><div class=\"blog_title\"><a href=\"url3\">第三篇博客</a></div></div></html>";

//第一步，将字符内容解析成一个Document类
Document doc = Jsoup.parse(html);

//第二步，根据我们需要得到的标签，选择提取相应标签的内容
Elements elements = doc.select("div[id=blog_list]").select("div[class=blog_title]");
for( Element element : elements ){
String title = element.text();
titles.add(title);
urls.add(element.select("a").attr("href"));

======================================================================
		Elements elements = doc.select(String str)
		div匹配div标签
		#ahhh 匹配id为ahhh的标签
		div#ahhh 匹配
		element.attr(String)获得属性值
		element.html()获得标签所包夹的内容
}
	 */
	
	/**
	 * 从本地获得html文档内容
	 * @param File f
	 * @return Document doc
	 * @throws IOException
	 */
	public static Document getContext(File f) throws IOException
	{
		String charsetName=CharsetDectector.getEncode(f);
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f),Charset.forName(charsetName)));
		
		String tmp=null;
		StringBuffer buffer=new StringBuffer();
		while((tmp=br.readLine())!=null)
		{
			buffer.append(tmp);
		}
		br.close();
		
		return Jsoup.parse(buffer.toString()) ;
	}

	
	/**
	 * 提取章节标题与内容
	 * @param Document doc
	 */
	public static NovChapter extractChapter(Document doc)
	{
		//删除<br>标签
//		doc.select("br").remove();
		
		//匹配class：.class
		//匹配标签tag：tag
		Elements title=doc.select("h1");
		//匹配ID：#id
		Elements content=doc.select("#content");
		
		//删除其他标签
//		Whitelist allowList=Whitelist.simpleText();
//		String content_fixed=Jsoup.clean(content.html(), allowList);
		
		List<TextNode> lines= content.textNodes();
		StringBuffer bf=new StringBuffer();
		for(TextNode node:lines)
		{
			bf.append(node.text());
			bf.append("\n");
		}
		
		String content_fixed=bf.toString();
//		content_fixed=content_fixed.replaceAll("&nbsp;"," ");
		return new NovChapter(title.html(),content_fixed);
	}
	/**
	 * 解析HTML文档，获得书籍信息
	 * @param doc
	 * @return NovInfo
	 */
	public static NovInfo extractBookInfo(Document doc)
	{
		String charset=doc.selectFirst("meta").attr("charset");
		if(charset.equals(""))
		{
			charset=doc.selectFirst("meta").attr("content");
			charset=charset.substring(charset.indexOf("charset=")+8);
		}
		if(charset.equals(""))charset=CharsetDectector.getEncode(doc.html());
		
	System.out.println(charset);
		Element info=doc.selectFirst("#maininfo");
		Elements bookName=info.select("h1");
		Elements introduction=info.select("#intro").select("p");
		
		String regexWritter="作\\s*者";
		String regexUpdate="最后更新";
		Elements writter=info.select("p:matches("+regexWritter+")");
		Elements updateTime=info.select("p:matches("+regexUpdate+")");
		
		String updateTime_str=updateTime.html();
		updateTime_str=updateTime_str.substring(updateTime_str.indexOf("：")+1).replace(" ","T");
		String writter_str=writter.html();
		writter_str=writter_str.substring(writter_str.indexOf("：")+1);
		
		return new NovInfo(bookName.html(), introduction.html()
				,LocalDateTime.parse(updateTime_str), writter_str,charset);
	}
	
	/**
	 * 提取章节列表
	 * @param doc
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public static LinkedList<URL> extractChapterList_Remote(URL url) throws IOException
	{
		Document doc=HtmlCatcher.getDoc(url.toString());
		
		/**
		 * 存储章节编号与章节文件地址
		 */
		LinkedList<URL> chapterList=new LinkedList<URL>();
		//是否为正文
		boolean isMainText=false;
		Elements dlList=doc.select("div#list").select("dd,dt");
		for(Element tag:dlList)
		{
			//跳过最新章节部分
			if(!isMainText)
			{//不是正文
				if(tag.tagName().equals("dt"))
				{
					if(tag.html().contains("正文"))
					{
						isMainText=true;
					}
				}
				continue;
			}
			//选择链接
			Element chapterLink=tag.select("a").first();
			String path = null;
			//这个可以直接获得绝对路径
			path=chapterLink.attr("abs:href");
//			//获得绝对路径，其实这个转换是不会出错的
//			try
//			{
//				path = url.toURI().toString()+getRelevantUrl(chapterLink.attr("href"));
//			} catch (URISyntaxException e1)
//			{
//				e1.printStackTrace();
//			}

//log.debug(path);
			try
			{
				URL pathUrl=new URL(path);
				chapterList.add(pathUrl);
			} catch (MalformedURLException e)
			{
				String bookName=doc.select("#maininfo").select("h1").html();
				throw new MalformedURLException(bookName+"("+url+")：解析章节路径失败");
			}
		}
		
		return chapterList;
	}
	
	/**
	 * 返回相对于书籍存储目录的相对路径
	 * 例如将形如"https://www.cnoz.org/0_1/751228.html"的Url
	 * 转化为"751228.html"
	 * @param url
	 * @return
	 */
	protected static String getRelevantUrl(String url)
	{
		//本身就符合
		if(!url.contains("/"))return url;
		
		//匹配751228.html
		String regex=".*[0-9]{0,10}\\.html";
		
		String tmp=url;
		int index=0;//第一个/的位置
		while(Pattern.matches(regex, tmp))
		{
			url=tmp;
			index=url.indexOf("/");
			//没有”/“了
			if(index<0)
			{
				break;
			}
			
			if(index==0)
			{
				//避免出现字符串开头为/导致的死循环
				tmp=tmp.substring(1);
			}else
			{
				tmp=tmp.substring(index);
			}
		}
//		System.out.println(url);
		return url;
	}
}





















