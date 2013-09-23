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

package ch.fetm.backuptools.common.datanode;

import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;

import ch.fetm.backuptools.common.model.Blob;
import ch.fetm.backuptools.common.model.BlobList;

public interface INodeDatabase {
	
	public abstract void addLineToIndexFiles(String line);

	public abstract String sendStringBuffer(StringBuffer sb);

	public abstract Blob sendFile(Path file);

	public abstract InputStream createInputStreamFromNodeName(String signature);

	public abstract Reader createInputStreamFromIndex();
	
	public abstract void initFS();

	public abstract boolean isFSInitialized();
}