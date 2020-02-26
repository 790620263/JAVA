package file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjByteTransformer {

	public static byte[] toArray(Object o) {
		byte[] b=null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);) {
			oos.writeObject(o);
			oos.flush();
			b=bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	public static Object toObj(byte[] b) {
		Object obj=null;
		try (ByteArrayInputStream bin = new ByteArrayInputStream(b);
			ObjectInputStream oin = new ObjectInputStream(bin);){
			obj=oin.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static void main(String[] args) {

	}

}
