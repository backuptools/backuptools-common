
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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import ch.fetm.backuptools.common.sha.SHA1;

public class BackupAgent {
	private NodeDatabase   _node_database    = null;
	private List<Backup> 	_backups      	  = new ArrayList<Backup>();
	
	private Tree pushDirectory(Path file)
	{
		Tree tree = new Tree();
		try(DirectoryStream<Path> directories = Files.newDirectoryStream(file)){
			for(Path childfile : directories){
				if(childfile.toFile().isFile()){
					Blob blob = _node_database.sendFile(childfile);
					tree.addNode(blob, childfile.getFileName().toString());						
				}else{
					Tree childTree = pushDirectory(childfile); 
					tree.addTree(childTree,childfile.getFileName().toString());
				}
			}				
		} catch (IOException e) {
			e.printStackTrace();
		}
		_node_database.sendStringBuffer(tree.buildData());
		return tree;

	}

	
	private void restoreTreeToDirectory(Tree root) {
		// TODO Auto-generated method stub
		
	}	

	public BackupAgent(NodeDatabase nodeDatabase) {
		_node_database = nodeDatabase;
	}
	
	public void backupDirectory(Path path){
		SHA1 sha = new SHA1();
		String signature;
		Backup backup = new Backup();
		Tree   root   = null;
		root = pushDirectory(path);
		_node_database.sendStringBuffer(root.buildData());
		backup.setRoot(root);
		signature = sha.SHA1SignStringBuffer(root.buildData()).toString();
		_backups.add(backup);
		_node_database.addLineToIndexFiles(backup.getDate().toString()+"\t"+signature+"\n");
	}

	public List<Backup> getListBackup() {
		HashMap<String, Date> list = new HashMap<String, Date>();
		List<Backup> backups = new ArrayList<Backup>();
		BufferedReader reader = new BufferedReader(_node_database.createInputStreamFromIndex());
		String line="";
		try {
			line = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringTokenizer token  = new StringTokenizer(line,"\t");
		
		Backup b = new Backup();
		return null;
	}

	public void setDatabase(NodeDatabase data) {
		this._node_database = data;
	}
}
