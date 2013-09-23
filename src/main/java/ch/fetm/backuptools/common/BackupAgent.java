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

import ch.fetm.backuptools.common.datanode.INodeDatabase;
import ch.fetm.backuptools.common.model.Backup;
import ch.fetm.backuptools.common.model.Blob;
import ch.fetm.backuptools.common.model.Tree;
import ch.fetm.backuptools.common.model.TreeInfo;
import ch.fetm.backuptools.common.tools.SHA1;

public class BackupAgent {
	private INodeDatabase   _node_database    = null;
	
	private Tree pushDirectory(Path file)
	{
		Tree tree = new Tree();
		try(DirectoryStream<Path> directories = Files.newDirectoryStream(file)){
			for(Path childfile : directories){
				TreeInfo tinfo = new  TreeInfo();
				if(childfile.toFile().isFile()){
					Blob blob = _node_database.sendFile(childfile);
					tinfo.type = TreeInfo.TYPE_BLOB;
					tinfo.SHA  = blob.getName();				
				}else{
					Tree childTree = pushDirectory(childfile); 
					tinfo.type = TreeInfo.TYPE_TREE;
					tinfo.SHA  = childTree.getName();
				}
				tinfo.name = childfile.getFileName().toString();
				tree.addTreeInfo(tinfo);		
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
	
	public void restore(List<TreeInfo> trees, String restore_path) {
		Path path = Paths.get(restore_path);
		
		for(TreeInfo ti : trees){
			if(ti.type.equals(TreeInfo.TYPE_BLOB)){
				InputStream inputstream = _node_database.createInputStreamFromNodeName(ti.SHA);
				try {
					Files.copy(inputstream, Paths.get(restore_path+FileSystems.getDefault().getSeparator()+ti.name));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(ti.type.equals(TreeInfo.TYPE_TREE)){
				Path treePath = Paths.get(path.toAbsolutePath()+FileSystems.getDefault().getSeparator()+ti.name);
				if(!treePath.toFile().exists()){
					try {
					  Files.createDirectory(treePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
					restore(getTreeInfosOf(ti.SHA).getAllTreeInfo(),treePath.toAbsolutePath().toString());
				}
				
			}	
		}
		

		

	}
	public Tree getTreeInfosOf(String sha) {
		Tree tree = new Tree(this._node_database.createInputStreamFromNodeName(sha));
		return tree;
	}
}
