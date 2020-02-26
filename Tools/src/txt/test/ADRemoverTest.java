package txt.test;
import java.io.File;

import org.junit.jupiter.api.Test;

//import org.junit.Test;

import txt.ADRemover;

class ADRemoverTest
{

	@Test
	void test_HandleAllTxt()
	{
//		fail("Not yet implemented");
		File f=new File("C:\\Users\\AtCraft\\OneDrive\\Documents\\Novel");
		System.out.println("Start");
		ADRemover adr=new ADRemover();
		adr.handleAllTxt(f, f.getParent());
		System.out.println("Finish");
	}

}
