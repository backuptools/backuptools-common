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

package ch.fetm.backuptools.common.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.ws.ServiceMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.fetm.backuptools.common.model.BlobList;

public class BlobListTest {
	BlobList list;
	Path filepath;
	@Before
	public void Setup(){
		filepath = null;
		try {
			filepath = Files.createTempFile("blob.txt", "backuptools");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		list = new BlobList(filepath);
	}
	
	@After
	public void TearDown(){
		try {
			Files.delete(filepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddAndCheckContain(){
		list.add("123456789");
		list.add("23456789");

		assertTrue(list.contains("123456789"));
		assertTrue(list.contains("23456789"));
		
		assertFalse(list.contains("2345678900"));
	}

}
