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

public class BackupAgent implements IBackupAgent {
	private INodeDatabase   _node_database    = null;
	private Path _source_directory;
	
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
		_node_database.sendTree(tree);
		return tree;

	}

	public BackupAgent(Path source_path, INodeDatabase vault) {
		_node_database = vault;
		_source_directory = source_path;
	}

	public void backupDirectory(Path path){
		Tree   root   = null;
		root = pushDirectory(path);
		_node_database.sendTree(root);
        Backup backup = new Backup(Calendar.getInstance().getTime().toString(), root.getName());
		_node_database.sendBackup(backup);
	}
	
	/* (non-Javadoc)
	 * @see ch.fetm.backuptools.common.IBackupAgent#getListBackups()
	 */
	@Override
	public List<Backup> getListBackups() {
		List<Backup> backups = new ArrayList<Backup>();
        //TODO A faire encore
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ch.fetm.backuptools.common.IBackupAgent#setDatabase(ch.fetm.backuptools.common.datanode.INodeDatabase)
	 */
	@Override
	public void setDatabase(INodeDatabase data) {
		this._node_database = data;
	}
	
	/* (non-Javadoc)
	 * @see ch.fetm.backuptools.common.IBackupAgent#restore(java.util.List, java.lang.String)
	 */
	@Override
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
	/* (non-Javadoc)
	 * @see ch.fetm.backuptools.common.IBackupAgent#getTreeInfosOf(java.lang.String)
	 */
	@Override
	public Tree getTreeInfosOf(String sha) {
		Tree tree = new Tree(this._node_database.createInputStreamFromNodeName(sha));
		return tree;
	}

	@Override
	public void doBackup() {
		backupDirectory(_source_directory);
	}
}
