/*	Copyright 2013 Florian Mahon <florian@faivre-et-mahon.ch>
 * 
 *    This file is part of backuptools.
 *    
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.fetm.backuptools.common;

import static org.junit.Assert.*;
import java.io.InputStream;
import org.junit.Test;

import ch.fetm.backuptools.common.tools.SHA1;
import ch.fetm.backuptools.common.tools.SHA1Signature;


public class SHA1Test {
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
