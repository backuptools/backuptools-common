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

package org.fetm.backuptools.common.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BlobList {
	private Path filepath;
	
	public BlobList(Path path) {
		filepath = path;
	}
	public void setBlobIndex(List<String> blobs) {
		OutputStream out = null;
		if(Files.exists(filepath)){
			try {
				Files.delete(filepath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Files.createFile(filepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out = new FileOutputStream(filepath.toFile());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(String blob : blobs){
			try {
				blob = blob+'\n';
				out.write(blob.toString().getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public List<String> getBlobIndex() {
		List<String> blobs = new ArrayList<String>();
		BufferedReader in = null;
		try {
			in  = new BufferedReader(new FileReader(filepath.toFile()));
		} catch (FileNotFoundException e) {
			try {
				Files.createFile(filepath);
				in = new BufferedReader(new FileReader(filepath.toFile()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try {
			String line = null;
			do{
				line = in.readLine();
				if(line != null)
					blobs.add(line);
			}while(line != null);	
		} catch (IOException e) {

		}
		

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return blobs;
	}
	public void add(String name) {
		List<String> blobs = getBlobIndex();
		blobs.add(name);
		setBlobIndex(blobs);
	}
	
	public boolean contains(String name){
		List<String> blob = getBlobIndex();
		return blob.contains(name);
	}
}
