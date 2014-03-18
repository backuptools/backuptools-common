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

package ch.fetm.backuptools.common.tools;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.fetm.backuptools.common.tools.ScpClient;

public class ScpClientTest {
	private ScpClient sftp;
	private String name = "test";
	private String host = "localhost";
	private String pass = "test";

	private String localname = "/home/florian/index.txt";
	private String remotename = "/home/test/index.txt" ;
	
	@Before
	public void Setup() {
		sftp = new ScpClient(host, name, pass);
	}
	
	@Test
	public void testAll(){	

		Path local = Paths.get(localname);
		if(!Files.exists(local)){
			try {
				Files.createFile(local);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(sftp.isExist(remotename)){
			sftp.rmFile(remotename);
		}
		
		sftp.put(localname, remotename);
		assertTrue(sftp.isExist(remotename));

		sftp.rmFile(remotename);
		assertFalse(sftp.isExist(remotename));
		
		sftp.put(localname, remotename);
		assertTrue(sftp.isExist(remotename));
		
		if(Files.exists(local))
		{
			try {
				Files.delete(local);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		assertFalse(Files.exists(local));
		
		sftp.get(remotename, localname);
		assertTrue(Files.exists(local));
	}
		
	@After
	public void tearDown(){
		
	}

}
