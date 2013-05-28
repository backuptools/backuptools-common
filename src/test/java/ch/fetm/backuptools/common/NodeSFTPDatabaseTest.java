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

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.fetm.backuptools.common.sha.SHA1;

public class NodeSFTPDatabaseTest {
	private INodeDatabase database;
	private Path db_location;
	
	@Before
	public void setup(){
		
		db_location = TestUtilities.createTemporyFile();
		
		database = new SchNodesDatabase(db_location,"localhost","test","test","/home/test/");
	}


	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void addLinetoIndexFile_createInputStream(){
		String line = "Line 1 test";
		BufferedReader input;

		database.addLineToIndexFiles(line);
		input = new BufferedReader(database.createInputStreamFromIndex());
		
		try {
			assertEquals(line, input.readLine());
		} catch (IOException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void sendFile_createInputStreamFromNodeName(){
		SHA1 sha = new SHA1();
		String filecontents = "Hello world and I test you";
		String filename = "file.txt";
		
		Path file = Paths.get( db_location.toAbsolutePath().toString()
								+FileSystems.getDefault().getSeparator()
								+filename);

		OutputStream out;
		try {
			out = new FileOutputStream(file.toFile());
			out.write(filecontents.getBytes());
			out.close();
			
			String blobname = sha.SHA1SignFile(file).toString();			
			
			database.sendFile(file);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(database.createInputStreamFromNodeName(blobname)));
			
			assertEquals(filecontents, in.readLine());
			in.close();
			
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertFalse(true);
		}
		

	}

	@Test
	public void sendStringBuffer(){
		SHA1 sha = new SHA1();
		String filecontents = "Hello world and I test you";
		StringBuffer sb     = new StringBuffer(filecontents);
		
		String blobname = sha.SHA1SignStringBuffer(sb).toString();			
		
		database.sendStringBuffer(sb);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(database.createInputStreamFromNodeName(blobname)));
		
		try {
			assertEquals(filecontents, in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
