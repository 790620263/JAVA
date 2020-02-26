package txt;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import info.monitorenter.cpdetector.io.*;

public class EncodeDectector
{
	  /**
     * 检测该地址下的文件编码
     * @param URL
     * @return Str_CharSet
     */
    public static String getEncode(URL url) {
        String charsetName = null;
        try {
            CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
            detector.add(new ParsingDetector(false));
            detector.add(JChardetFacade.getInstance());
            detector.add(ASCIIDetector.getInstance());
            detector.add(UnicodeDetector.getInstance());
            
            java.nio.charset.Charset charset = null;
            charset = detector.detectCodepage(url);
            if (charset != null&&charset.name()!="void") {
                charsetName = charset.name();
            } else {
                charsetName = "UTF-8";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return charsetName;
    }
    
    public static String getEncode(File file) {
        String charsetName="UTF-8";
		try
		{
			charsetName = getEncode(file.toURI().toURL());
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
        return charsetName;
    }
    public static String getEncode(String filePath) {
        String charsetName=getEncode(new File(filePath));
        return charsetName;
    }
}
