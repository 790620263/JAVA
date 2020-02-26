package securit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import file.ObjByteTransformer;


//消息摘要算法
/**
 * 
 * @author AtCraft<br>
 * 这个类用于计算SHA与MD5与CRC32值<br>
AllAglgorithm:<br>
MD2
MD5
SHA-1
SHA-224
SHA-256
SHA-384
SHA-512/224
SHA-512/256
SHA3-224
SHA3-256
SHA3-384
SHA3-512
 */
public class MsgDigestKit {

	public static final String ALGORITHM_SHA3_256="SHA3-256";
    public static final String ALGORITHM_MD5="MD5";

    /**
     * 
     * @param obiect
     * @return String （摘要）
     */
	public static String calcSHA256(Object o)
	{
		byte[] result=null;
		try {
			MessageDigest md=MessageDigest.getInstance(ALGORITHM_SHA3_256);
			md.update(ObjByteTransformer.toArray(o));
			result=md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new BigInteger(result).toString(16);
	}
	public static String calcMD5(Object o)
	{
		byte[] result=null;
		try {
			MessageDigest md=MessageDigest.getInstance(ALGORITHM_MD5);
			md.update(ObjByteTransformer.toArray(o));
			result=md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new BigInteger(result).toString(16);
	}
	public static void main(String[] args) {
		System.out.println(calcSHA256("HELLOslhj;ho;jklk'八八八八jhshgsgsgsdgsdgsdh"));
		System.out.println(calcMD5("Hhgsghshsdsgsdgsdgsdh"));
	}
	static long calcCRC32(String file) throws IOException
	{
		File f=new File(file);
		
		if(!f.isFile())
		{
			throw new IOException("Not a file");
		}
		if(! f.exists())
		{
			throw new  FileNotFoundException("文件不存在");
		}
		
		FileInputStream fin=new FileInputStream(f);
		BufferedInputStream in=new BufferedInputStream(fin);
		
		int len=0;//本次读取到的字节数
		byte[] data=new byte[8192];
		
		CRC32 c=new CRC32();
		
		while((len=in.read(data))>0)
		{
			c.update(data,0,len);
		}
		
		in.close();
		fin.close();
		
		return c.getValue();
	}

}
