package securit;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *用于加解密的类
 *
 */
public class AESCipherer {

	public static Key createKey(String password) throws Exception
	{
		if(password.length()>32)throw new Exception("密码长度超过32位");
		
		byte[] pw=password.getBytes("utf-8");
		byte[] keyBytes=new byte[32];
		//填充数组
		for(int i=0;i<pw.length;i++)
		{
			keyBytes[i]=pw[i];
		}
		
		Key key = new SecretKeySpec(keyBytes, "AES");
		return key;
	}
	/**
	 * 
	 * @param password
	 * @return 一个用于加密的密码器
	 * @throws Exception：密码长度超过32位
	 */
	public static  Cipher createEncodeCipher(String password) throws Exception
	{
		//生成密匙
		Key key=createKey(password);
		
		//构造cipher并初始化
		Cipher c=Cipher.getInstance("AES/OFB32/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, key);
		
		return c;
	}
	/**
	 * 
	 * @param password
	 * @return 一个用于解密的密码器
	 * @throws Exception：密码长度超过32位
	 */
	public static  Cipher createDecodeCipher(String password) throws Exception
	{
		//生成密匙
		Key key=createKey(password);
		
		//构造cipher并初始化
		Cipher c=Cipher.getInstance("AES/OFB32/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, key);
		
		return c;
	}
	
	
	
	/**
	 * 加密
	 * @param String
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] strEncodeAES(String data,String password) throws Exception
	{
		byte[] result=null;
		
		try {
			//获得一个用于加密的密码器
			Cipher c=createEncodeCipher(password);
			
			//得到加密结果，一个字节数组
			result = c.doFinal(data.getBytes());
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		return result;
	}
    /**
     * 解密
     * @param data
     * @param password
     * @return
     * @throws Exception
     */
	public static byte[] strDecodeAES(byte[] cryptedData,String password) throws Exception
	{
		byte[] result=null;
		
		try {
			//获得一个用于解密的密码器
			Cipher c=createDecodeCipher(password);
			
			//得到解密结果，一个字节数组
			result = c.doFinal(cryptedData);
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 加密流
	 * @param InputStream 普通输入流
	 * @param password
	 * @return CipherInputStream加密输入流
	 * @throws Exception
	 */
	public static CipherInputStream getAESInputStream(InputStream in,String password) throws Exception
	{
		//获得一个用于加密的密码器
		Cipher c=createEncodeCipher(password);

		return new CipherInputStream(in, c);
	}
	
	/**
	 * 解密流
	 * @param OutputStream 普通输出流
	 * @param password
	 * @return CipherOutputStream加密输出流
	 * @throws Exception
	 */
	public static CipherOutputStream getAESOutputStream(OutputStream out,String password) throws Exception
	{
		//获得一个用于解密的密码器
		Cipher c=createDecodeCipher(password);

		return new CipherOutputStream(out, c);
	}
	
	public static void main(String[] args) {
		try {
			byte[] m=strEncodeAES("hello", "qwertyuiopasdfghjklzxcvbnm123456");
			System.out.println(new String(strDecodeAES(m,"qwertyuiopasdfghjklzxcvbnm123456")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
