
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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ch.fetm.backuptools.common.sha.SHA1;
import ch.fetm.backuptools.common.sha.SHA1Signature;

public class NodeDatabase {
	
	private String _vault_location;

	public NodeDatabase(String vault_location) {
		_vault_location = vault_location;
	}
	
	public void addLineToIndexFiles(String line){
		FileOutputStream out = null;
		Path file = Paths.get(_vault_location+FileSystems.getDefault().getSeparator()+"index.txt");
		try {
			out = new FileOutputStream(file.toFile(),true);
		} catch (FileNotFoundException e1) {
			try {
				Files.createFile(file);
				out = new FileOutputStream(file.toFile(),true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			out.write(line.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendStringBuffer(StringBuffer sb) {
		SHA1 sha = new SHA1();
		SHA1Signature sign = sha.SHA1SignStringBuffer(sb);
		Path file = Paths.get(_vault_location+FileSystems.getDefault().getSeparator()+sign.toString());
		if(! Files.exists(file)) {
			try {
				Files.copy(new ByteArrayInputStream(sb.toString().getBytes()), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Blob sendFile(Path file){
		SHA1 sha  = new SHA1();
		Blob blob = null;
	
		String blobName =sha.SHA1SignFile(file).toString();
		
		Path blobfile = Paths.get(_vault_location+FileSystems.getDefault().getSeparator()+blobName);
		if(! Files.exists(blobfile)) {
			try {
				Files.copy(file, blobfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		blob = new Blob(blobName);
		return blob;
	}
	
	public InputStream createInputStreamFromNodeName(String signature){
		Path file = Paths.get(_vault_location+FileSystems.getDefault().getSeparator()+signature);
		try {
			return new FileInputStream(file.toFile());
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public Reader createInputStreamFromIndex() {
		try {
			return new FileReader(_vault_location+FileSystems.getDefault().getSeparator()+"index.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String getName() {
		return	_vault_location;
	}	
}
