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

package org.fetm.backuptools.common.datanode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import org.fetm.backuptools.common.model.Backup;
import org.fetm.backuptools.common.model.Blob;
import org.fetm.backuptools.common.model.Tree;

public interface INodeDatabase {
    void sendTree(Tree tree);
	Blob sendFile(Path file);
	InputStream createInputStreamFromNodeName(String signature);
    void sendBackup(Backup backup);
    List<Backup> getBackups() throws IOException;
}