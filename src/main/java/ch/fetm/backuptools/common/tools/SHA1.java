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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {


	public SHA1Signature SHA1SignFile(Path file){
		InputStream f=null;
		try {
			f = new FileInputStream(file.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SHA1SignInputStream(f);
	}
	
	public SHA1Signature SHA1SignInputStream(InputStream in){
		int buffer_size = 1024;
		int nread = 0;
		byte[] data = new byte[buffer_size];
		
		MessageDigest m = null;
	
		try {
			m = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		try {
			while((nread = in.read(data)) != -1){
				m.update(data,0,nread);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new SHA1Signature(m.digest());
		
	}
	
	public SHA1Signature SHA1SignStringBuffer(StringBuffer string_buffer){
		InputStream in = new ByteArrayInputStream(string_buffer.toString().getBytes());
		return SHA1SignInputStream(in);
	}
	
}
