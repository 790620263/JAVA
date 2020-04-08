package txt;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    
    public static String getEncode(File file) throws FileNotFoundException {
        String charsetName="UTF-8";
		
        if(!file.exists())throw new FileNotFoundException();
        
		StringBuffer br=new StringBuffer();
		try {
			BufferedReader reader=new BufferedReader(new FileReader(file));
			br.append(reader.readLine());
			br.append(reader.readLine());
			br.append(reader.readLine());
			br.append(reader.readLine());
			br.append(reader.readLine());
			
			reader.close();
		}catch(EOFException e)
		{
//			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		charsetName = getEncode(br.toString());
        return charsetName;
    }
    public static String getEncode(String text) {
    	String charsetName = null;
        try {
            CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
            detector.add(new ParsingDetector(false));
            detector.add(JChardetFacade.getInstance());
            detector.add(ASCIIDetector.getInstance());
            detector.add(UnicodeDetector.getInstance());
            
            java.nio.charset.Charset charset = null;
            charset = detector.detectCodepage(new ByteArrayInputStream(text.getBytes()), 100);
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
//    @Test
//    public void testGetEncodingText()
//    {
//    	System.out.println(getEncode("个名字里的“芙蓉”，取的是“芙蓉冻鸡血石”的芙蓉。"));
//    }
    
}
