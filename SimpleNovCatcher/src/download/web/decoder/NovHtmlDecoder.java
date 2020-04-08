package download.web.decoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import download.dataPack.NovChapter;
import download.dataPack.NovInfo;
import download.web.catcher.HtmlCatcher;
import tool.CharsetDectector;

/**
 * 用以解析Html
 * 
 * @author AtCraft
 *
 */
public class NovHtmlDecoder
{
	/*
	 * jousp的一些说明 摘自：https://www.ibm.com/developerworks/cn/java/j-lo-jsouphtml/
	 * 
	 * 
	 * //假设我们获取的HTML的字符内容如下 String html =
	 * "<html><div id=\"blog_list\"><div class=\"blog_title\"><a href=\"url1\">第一篇博客</a></div><div class=\"blog_title\"><a href=\"url2\">第二篇博客</a></div><div class=\"blog_title\"><a href=\"url3\">第三篇博客</a></div></div></html>"
	 * ;
	 * 
	 * //第一步，将字符内容解析成一个Document类 Document doc = Jsoup.parse(html);
	 * 
	 * //第二步，根据我们需要得到的标签，选择提取相应标签的内容 Elements elements =
	 * doc.select("div[id=blog_list]").select("div[class=blog_title]"); for( Element
	 * element : elements ){ String title = element.text(); titles.add(title);
	 * urls.add(element.select("a").attr("href"));
	 * 
	 * ======================================================================
	 * Elements elements = doc.select(String str) div匹配div标签 #ahhh 匹配id为ahhh的标签
	 * div#ahhh 匹配 element.attr(String)获得属性值 element.html()获得标签所包夹的内容 }
	 */

	/**
	 * 从本地获得html文档内容
	 * 
	 * @param File f
	 * @return Document doc
	 * @throws IOException
	 */
	public static Document getContext(File f) throws IOException
	{
		String charsetName = CharsetDectector.getEncode(f);
		BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream(f), Charset.forName(charsetName)));

		String tmp = null;
		StringBuffer buffer = new StringBuffer();
		while ((tmp = br.readLine()) != null)
		{
			buffer.append(tmp);
		}
		br.close();

		return Jsoup.parse(buffer.toString());
	}

	/**
	 * 提取章节标题与内容
	 * 
	 * @param Document doc
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 */
	public static NovChapter extractChapter(Document doc) throws IOException, IllegalAccessException
	{
		// 删除<br>标签
//		doc.select("br").remove();

		// 匹配class：.class
		// 匹配标签tag：tag
		Elements title = doc.select("h1");
		// 匹配ID：#id，或匹配属性[id=。。。]
		String matchReg="div[id*=ontent],#BookText";//可能是content或者Content
		Elements rootContent = doc.select(matchReg);
//
//		int subContentNum=0;
//		Elements contents= rootContent.select("div[id*=content]");
		
//		 删除自定义标签
		Whitelist allowList=Whitelist.basic();
		allowList.addAttributes("div", "class","id");
		String content_cleaned=Jsoup.clean(rootContent.html(), allowList);
		rootContent=Jsoup.parse(content_cleaned).getAllElements();
		
//		for(int index=0;index<rootContent.size();index++)
//		{//删除带有汉字的标签
//			if(rootContent.get(index).tagName().matches(".*[u4e00-u9fa5]*.*"))rootContent.remove(index);
//		}
		
		
		StringBuffer bf = new StringBuffer();
		if(rootContent.select("script").size()>0 || rootContent.select("br").size()==0) {//是动态网页，Josup无法解析
			throw new IllegalAccessException("这是动态网页，Josup无法解析");
		}
		
		
		Elements subEle=rootContent,temp=null;
		{
			for(Element ele:rootContent)
			{
				String reg=ele.tagName()+" "+matchReg;
				temp=ele.select(reg);
				if(temp!=null && ! temp.isEmpty())
				{
					subEle=temp;
				}
			}
		}
		
		
		
		List<TextNode> lines=subEle.textNodes();
		for (TextNode node : lines)
		{
            //删除广告行
//            if(!node.text().toLowerCase().matches(".*ww\\..*com.*")){
			bf.append(node.text());
			bf.append("\n");
            
		}
		String content_fixed = bf.toString();
//		content_fixed=content_fixed.replaceAll("&nbsp;"," ");
		return new NovChapter(title.html(), content_fixed);
	}


	/**
	 * 解析HTML文档，获得书籍信息
	 * 
	 * @param doc
	 * @return NovInfo
	 */

	
	public static NovInfo extractBookInfo(Document doc)
	{
		System.out.println("########Decode########");
		

		String charset=getCharset(doc);

		Element info = doc.selectFirst("#maininfo,.bookinfo");

		// 匹配书名
		String bookName_str = "未知";
		if (info != null)
		{
			Elements bookName = info.select("h1");
			if (bookName != null)
			{
				bookName_str = bookName.text();
			}
		}
		if (bookName_str.equals("未知"))
		{
			bookName_str = doc.selectFirst("title").text();
			if(bookName_str.contains("最新"))
			{
				bookName_str=bookName_str.split("最新")[0];
			}
		}

		System.out.println("#INFO：Name=" + bookName_str + "  charset=" + charset);

//匹配简介
		String intro_str = "未知";
		if (info != null)
		{
			Element introduction = info.selectFirst("#intro,.intro");
			if (introduction != null)
				intro_str = introduction.text();
		}
//		System.out.println("#INFO：Introduction=" + intro_str);

		// 匹配作者
		String writter_str = "未知";

		String[] regexWritters = new String[] { "作\\s*者" };
		Elements writter = null;
		if (info != null)
		{
			for (String regexWritter : regexWritters)
			{
				writter = info.select("p:matches(" + regexWritter + ")," + "em:matches(" + regexWritter + ")");
				if (writter != null)
				{
					writter_str = writter.text();
					if (!writter_str.equals(""))
					{
						writter_str = writter_str.substring(writter_str.indexOf("者：") + 2);
						break;
					}

				}
			}
		}

//		System.out.println("#INFO：作者=" + writter_str);

		// 匹配：更新时间，最后更新
		String updateTime_str = "未知";
		if (info != null)
		{
			String[] regexUpdates = new String[] { "更新时间", "最后更新" };
			Elements updateTime = null;
			for (String regexUpdate : regexUpdates)
			{
				updateTime = info.select("p:matches(" + regexUpdate + ")");

				if (updateTime != null)
				{
					if (updateTime.isEmpty())
                    {
						updateTime_str = "1970-01-01";
					}
                    else 
                    {
                        updateTime_str = updateTime.text();
                        updateTime_str = updateTime_str
                            .substring(updateTime_str.indexOf(regexUpdate + "：") + 1 + regexUpdate.length())
                            .replace(" ", "T");
                        break;
                    }
				}
			}
		}

//		System.out.println("#INFO：更新时间=" + updateTime_str);

		return new NovInfo(bookName_str, intro_str, updateTime_str, writter_str, charset);
	}

	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 提取章节列表
	 * 
	 * @param doc
	 * @return
	 * @throws MalformedURLException 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static LinkedList<URL> extractChapterList_Remote(URL url) throws MalformedURLException, IOException
	{
		Document doc = HtmlCatcher.getDoc(url);

		/**
		 * 存储章节编号与章节文件地址
		 */
		LinkedList<URL> chapterList = new LinkedList<URL>();

		Elements dlList = doc.select("div#list").select("dd,dt");


		//检查是否是逻辑列表类型的
		if (dlList.size() > 0 && dlList.eachText().get(dlList.size()-1).contains("章"))
		{

			// 判断是否为正文
			boolean isMainText = true;

			// 检查是否有正文和最新章节两栏
			for (Element tag : dlList.select("dt"))
			{
				if (tag.tagName().equals("dt"))
					if (tag.text().contains("最新章节"))
					{
						isMainText = false;
						break;
					}
			}

			for (Element tag : dlList)
			{
				// 跳过最新章节部分
				if (!isMainText)
				{// 不是正文
					if (tag.tagName().equals("dt"))
					{
						if (!tag.text().contains("最新章节"))
						{
							isMainText = true;
						}
					}
					continue;
				}

				// 选择链接
				Element chapterLink = tag.selectFirst("a");
				if (chapterLink == null)
					continue;
				String path = null;
				// 这个可以直接获得绝对路径
				path = chapterLink.attr("abs:href");

				URL pathUrl = new URL(path);
				chapterList.add(pathUrl);

			}

		}
        else
		{
			Elements tdList = doc.select("td");
            //如果是表格类型的
            if (tdList.size() > 0 && tdList.eachText().get(tdList.size()-1).contains("章"))
            {
                for (Element td:tdList)
                {
                    Element chapterLink = td.selectFirst("a");
                    if (chapterLink == null)
                        continue;
                    String path  = chapterLink.attr("abs:href");

                    URL pathUrl = new URL(path);
                    chapterList.add(pathUrl);
                }

            }
            else 
            {//其他情况
                Elements aList = doc.select("a");
                //如https://www.taiuu.com/0/741


                // 判断是否为正文
                boolean isMainText = false;

                for (Element tag : aList)
                {
                    if (tag.html().matches(".*第.*章.*"))
                    {
                        // 跳过最新章节部分
                        if (!isMainText)
                        {// 不是正文
                            if (tag.text().contains("第1章")||tag.text().contains("第一章"))
                            {
                                isMainText = true;
                            }

                            if (!isMainText)  continue;
                        }
//                        System.out.println(tag.html());
                        // 选择链接
                        Element chapterLink = tag.selectFirst("a");
                        if (chapterLink == null)
                            continue;
                        String path = null;
                        // 这个可以直接获得绝对路径
                        path = chapterLink.attr("abs:href");

                        URL pathUrl = new URL(path);
                        chapterList.add(pathUrl);
                    }
                }
            }
        }
		return chapterList;

	}

	
	
	

	/**
	 * 获得网页字符编码
	 * @param doc
	 * @return
	 */
	public static String getCharset(Document doc)
	{
		// 匹配字符集
		Elements meta = doc.select("meta");
		String charset =meta.attr("charset");
		if (charset.equals(""))
		{
			for (Element e : meta)
			{
				 String contentType=e.attributes().toString();
	                if (contentType.contains("utf-8")){
	                    return "UTF-8";
	                }
	                if(contentType.contains("gb2312")){
	                    return "GB2312";
	                }
	                if (contentType.contains("gbk")){
	                    return "GBK";
	                }
				
                String temp = null;
				if ((temp = e.attr("content")).contains("charset"))
				{
					charset = temp.substring(temp.indexOf("charset") + 8);
					break;
				}
				
			}
//					正则获取字符编码
            String regex = ".*charset=([^;]*).*";	
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(meta.html());
            if(matcher.find())	
            	return matcher.group(1);	
            else		
            	return null;
		}
		if (charset.equals(""))
			charset = CharsetDectector.getEncode(meta.text());
		
		return charset;
	}
	
	
	
	
	/**
	 * 返回相对于书籍存储目录的相对路径 例如将形如"https://www.cnoz.org/0_1/751228.html"的Url
	 * 转化为"751228.html"
	 * 
	 * @param url
	 * @return
	 */
	protected static String getRelevantUrl(String url)
	{
		// 本身就符合
		if (!url.contains("/"))
			return url;

		// 匹配751228.html
		String regex = ".*[0-9]{0,10}\\.html";

		String tmp = url;
		int index = 0;// 第一个/的位置
		while (Pattern.matches(regex, tmp))
		{
			url = tmp;
			index = url.indexOf("/");
			// 没有”/“了
			if (index < 0)
			{
				break;
			}

			if (index == 0)
			{
				// 避免出现字符串开头为/导致的死循环
				tmp = tmp.substring(1);
			}
            else
			{
				tmp = tmp.substring(index);
			}
		}
		return url;
	}
}
