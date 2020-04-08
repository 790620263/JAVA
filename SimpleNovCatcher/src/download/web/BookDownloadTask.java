package download.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import download.ChapterDownloadTask;
import download.dataPack.NovChapter;
import download.dataPack.NovInfo;
import download.web.catcher.HtmlCatcher;
import download.web.decoder.NovHtmlDecoder;
import tool.CharsetDectector;

public class BookDownloadTask implements Callable<Boolean> {
	protected String novSavePath = System.getProperty("user.dir") + File.separator + "autoUpdate";
	URL url;// 小说的首页
	NovInfo info;
	PrintWriter localFileWriter;

	/**
	 * 
	 * @param url 小说的首页链接
	 * @throws IOException
	 */
	public BookDownloadTask(String url) throws IOException {
		this.url = new URL(url);
	}

	public BookDownloadTask(String url, String bookSavePath) throws IOException {
		this(url);
		this.novSavePath = bookSavePath + File.separator + "autoUpdate";
	}

	/**
	 * @return 是否读取了新章节
	 * @param url
	 * @param sinceIndexNum 从这一章开始下载
	 * @param line          从这一行开始写入
	 */
	volatile int time_out_times = 0;// 超时次数
	protected static final int TIMEOUT_TIMES_ALLOWED=3;

	public boolean multi_download_since(int sinceChapterIndex, long line) throws ExecutionException {
		try {
//			LocalTime t1 = LocalTime.now();
//			System.out.println(t1);

			createLocalWritter_cleanLocalFile(line);

			LinkedList<URL> ChapterAddressList = NovHtmlDecoder.extractChapterList_Remote(url);

//			LocalTime t2 = LocalTime.now();
//			System.out.println(t2);
//			System.out.println(t2.getSecond() - t1.getSecond());

			if (sinceChapterIndex > ChapterAddressList.size()) {
				System.err.println("指定的起始章节数不合法");
				return false;
			}

//			LocalTime t3 = LocalTime.now();
//			System.out.println(t3);
//			System.out.println(t3.getSecond() - t2.getSecond());

			try {
				dl(sinceChapterIndex, ChapterAddressList, true);
			} catch (IllegalAccessException e)// 不处理JS则无法解析
			{
				try {
					dl(sinceChapterIndex, ChapterAddressList, true);
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {// 读写失败则以处理JS的方式再试一次
				System.err.println("DownloadERR: " + e.getMessage() + " ,ReTrying");
				createLocalWritter_cleanLocalFile(line);
				try {
					dl(sinceChapterIndex, ChapterAddressList, true);
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			localFileWriter.close();
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 
	 * @param line 将line行以后的文字删除，防止因为ERROR导致上次写入的章节不完整
	 * @throws IOException
	 */
	protected void createLocalWritter_cleanLocalFile(long line) throws IOException {
		// 创建本地文件追加s写入流
		File file = new File(novSavePath + File.separator + info.getName().replaceAll(" ","") + ".txt");
		file.getParentFile().mkdirs();// 防止路径不存在

		// 如果已有本地文件
		if (file.exists() && file.length() > 0) {
			// 读出前line行
			LinkedList<String> lines = new LinkedList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			for (long nowLine = 1; nowLine <= line; nowLine++) {
				lines.add(reader.readLine());
			}
			reader.close();

			file.delete();

			localFileWriter = new PrintWriter(new FileOutputStream(file, true), true);

			// 将前line行写回，（即将后面的行删除）
			for (long nowLine = 1; nowLine <= line; nowLine++) {
				localFileWriter.println(lines.poll());
			}

		} else {
			// sinceChapterIndex=1;
			localFileWriter = new PrintWriter(new FileOutputStream(file, true), true);
			write(info);
		}
	}

	/**
	 * 下载文档
	 * 
	 * @param sinceChapterIndex
	 * @param ChapterAddressList
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws ExecutionException
	 */
	protected void dl(int sinceChapterIndex, LinkedList<URL> ChapterAddressList, boolean handleJS)
			throws IllegalAccessException, InterruptedException, IOException, ExecutionException {
		int newReadChapterNum = 0;

		// 16个章节下载线程
		ExecutorService pool = Executors.newFixedThreadPool(16);
		LinkedList<Future<NovChapter>> chapterList = new LinkedList<Future<NovChapter>>();

		// 第几章节
		int index = 1;

		for (URL aChapterLink : ChapterAddressList) {
			if (index < sinceChapterIndex) {
				index++;
				continue;
			}

			int i = (int) (Math.random() * 100);//// 做一个随机延时，防止网站屏蔽
			Thread.sleep(i);

			newReadChapterNum++;

			ChapterDownloadTask task = new ChapterDownloadTask(aChapterLink, handleJS);
			Future<NovChapter> future = pool.submit(task);
			chapterList.add(future);

			Future<NovChapter> f = chapterList.peek();
			if (f.isDone()) {
				System.out.println("name=" + info.getName() + "；章节数" + index + "：" + f.get().getTitle());
				write(f.get(), index);
				chapterList.remove();
				index++;
			}
		}

		while (chapterList.size() > 0) {
			Future<NovChapter> future = chapterList.poll();
			write(future.get(), index);

			System.out.println("name=" + info.getName() + "；章节数" + index + "：" + future.get().getTitle());
			index++;
		}
		localFileWriter.close();
		newReadChapterNum--;
		System.out.println(info.getName() + "###更新章节数=" + newReadChapterNum);

	}

	/**
	 * @param url
	 * @return boolean是否读取了新章节
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public boolean multi_download_since_last() throws MalformedURLException, IOException {

		try {
			// System.out.println(novSavePath+File.separator+info.getName()+".txt");
			long[] returnData = getLastLocalChapterIndexAndLine();
			int chapterNum = (int) returnData[0];
			long line = returnData[1];
			return multi_download_since(chapterNum, line - 1);
		} catch (ExecutionException e) {
			//超时重试
			if (time_out_times <= TIMEOUT_TIMES_ALLOWED && e.getMessage().toLowerCase().contains("time")) {
				System.err.println("Time out,超时次数=" + time_out_times);
				time_out_times++;
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				multi_download_since_last();
			} else {
				e.printStackTrace();
				return false;
			}
		}
		return false;

	}

	/**
	 * 获取本地最新章节数，从第几行开始写入的行数
	 * 
	 * @return chapterNum=returnData[0];lineNum=returnData[1];
	 * @throws IOException
	 * @throws ExecutionException
	 */
	protected long[] getLastLocalChapterIndexAndLine() throws IOException, ExecutionException {

		File f = new File(novSavePath + File.separator + info.getName() + ".txt");

		if (!f.exists())
			multi_download_since(0, 0);
//-------------------------确定本地文件的最新章节数及相应文本行数------------------------------------
		String charset = CharsetDectector.getEncode(f);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), charset));
		// 章节数
		int chapterNum = 0;
		long lineindex = 0, tmp = 0;

		String content = null;
		String regex = "第[1-9][0-9]{0,3}章.*";
		// 创建标题匹配模式
		Pattern pattern = Pattern.compile(regex);

		while ((content = reader.readLine()) != null) {
			tmp++;

			boolean matchAny = false;
			Matcher matcher = pattern.matcher(content);
			matchAny = matcher.matches();

			if (content.length() > 0) {
				if (matchAny)// 是章节名
				{
					chapterNum++;
					lineindex = tmp;
				} else {
					continue;
				}
			}
		}
		reader.close();

		return new long[] { chapterNum, lineindex };
	}

	synchronized
	protected void initInfo() throws IOException {
		info = NovHtmlDecoder.extractBookInfo(HtmlCatcher.getDoc(url));
	}
	

	/**
	 * 将章节写入本地
	 * 
	 * @param str
	 * @throws IOException
	 */

	synchronized void write(NovInfo info) throws IOException {
		localFileWriter.println("书名：" + info.getName());
		localFileWriter.println("作者：" + info.getWritter());
		localFileWriter.println("最后更新：" + info.getUpdateTime());
		localFileWriter.println("简介：" + info.getIntroduction());
		localFileWriter.flush();
	}

	synchronized void write(NovChapter chapter, int index) throws IOException {
//System.out.println(chapter.getTitle());	
		localFileWriter.println();
		localFileWriter.print("第" + index + "章   ");
		localFileWriter.println(chapter.getTitle());
		localFileWriter.println();
		localFileWriter.println(chapter.getContent());
		localFileWriter.flush();
	}

	public Boolean call() throws IOException {
		initInfo();
		boolean isSuccess = multi_download_since_last();
		return isSuccess;
	}
}
