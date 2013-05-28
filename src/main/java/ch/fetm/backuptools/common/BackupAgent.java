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
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import ch.fetm.backuptools.common.sha.SHA1;

public class BackupAgent {
	private INodeDatabase   _node_database    = null;
	
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

	public BackupAgent(INodeDatabase nodeDatabase) {
		_node_database = nodeDatabase;
	}
	
	public void backupDirectory(Path path){
		SHA1 sha = new SHA1();
		String signature;
		Tree   root   = null;
		root = pushDirectory(path);
		signature = _node_database.sendStringBuffer(root.buildData());
		_node_database.addLineToIndexFiles(Calendar.getInstance().getTime().toString()+"\t"+signature+"\n");
	}

	public List<Backup> getListBackups() {
		List<Backup> backups = new ArrayList<Backup>();
		BufferedReader reader = new BufferedReader(_node_database.createInputStreamFromIndex());
		String line="";
		Backup backup = null;
		try {
			do{
				line = reader.readLine();
				if(line != null){
					StringTokenizer token  = new StringTokenizer(line,"\t");	
					backup = new Backup(token.nextToken(),token.nextToken());
					backups.add(backup);
				}
			}while(line != null);
		} catch (IOException e) {
		}	
		return backups;
	}

	public void setDatabase(INodeDatabase data) {
		this._node_database = data;
	}

	public void restore(TreeInfo tree, String restore_path) {
		Path path = Paths.get(restore_path);
		
		if(tree.type.equals(TreeInfo.TYPE_BLOB)){
			InputStream inputstream = _node_database.createInputStreamFromNodeName(tree.SHA);
			try {
				Files.copy(inputstream, Paths.get(restore_path+FileSystems.getDefault().getSeparator()+tree.name));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(tree.type.equals(TreeInfo.TYPE_TREE)){
			List<TreeInfo> trees = getTreeInfosOf(tree.SHA);
			
			for(TreeInfo treechild : trees){
				Path treePath = Paths.get(path.toAbsolutePath()+FileSystems.getDefault().getSeparator()+tree.name);
				if(!treePath.toFile().exists()){
					try {
						Files.createDirectory(treePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				restore(treechild,treePath.toAbsolutePath().toString());
			}
			
		}
		

	}

	public List<TreeInfo> getTreeInfosOf(String sha) {
		List<TreeInfo> trees = Trees.get(this._node_database.createInputStreamFromNodeName(sha));
		return trees;
	}
}
