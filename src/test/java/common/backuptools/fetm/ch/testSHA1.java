package common.backuptools.fetm.ch;

import static org.junit.Assert.*;
import java.io.InputStream;
import org.junit.Test;

import ch.fetm.backuptools.common.sha.SHA1;
import ch.fetm.backuptools.common.sha.SHA1Signature;


public class testSHA1 {
	@Test
	public void testSignature() {
		StringBuffer sb;
		SHA1 sha1 = new SHA1();
		SHA1Signature signature = null;
		
		InputStream f = null;
		
		try {
			f = getClass().getClassLoader().getResourceAsStream("file.txt");
			signature = sha1.SHA1SignInputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sb = signature.getStringBuffer();		
		
		assertEquals(sb.toString(),"f874afa73be7027485fd3e99e6e729c38ba9aeee");
	}

}
