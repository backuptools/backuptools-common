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

import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;

public class NodeSwiftDatabase implements INodeDatabase {
	
	public NodeSwiftDatabase(){
	}
	
	@Override
	public void addLineToIndexFiles(String line) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendStringBuffer(StringBuffer sb) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Blob sendFile(Path file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream createInputStreamFromNodeName(String signature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader createInputStreamFromIndex() {
		// TODO Auto-generated method stub
		return null;
	}

}
