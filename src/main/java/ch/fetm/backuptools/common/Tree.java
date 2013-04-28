
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


import java.util.HashMap;

import ch.fetm.backuptools.common.sha.SHA1;
import ch.fetm.backuptools.common.sha.SHA1Signature;


public class Tree{
	
	private HashMap<Blob, String> _hash_blobs = new HashMap<Blob, String>();
	private HashMap<Tree, String> _hash_trees = new HashMap<Tree, String>();
	
	public StringBuffer buildData(){
		StringBuffer out  = new StringBuffer();
		for(Blob blob : _hash_blobs.keySet()){
			out.append("blob"+"\t"+blob.getName()+"\t"+_hash_blobs.get(blob)+"\n");
		}
		for(Tree tree : _hash_trees.keySet()){
			out.append("tree"+"\t"+tree.getName()+"\t"+_hash_trees.get(tree)+"\n");
		}
		return out;
	}
	
	private String getName() {		
		SHA1 sha = new SHA1();
		SHA1Signature signature =  sha.SHA1SignStringBuffer(buildData());
		return signature.toString();
	}
	
	public void addNode(Blob blob, String name) {
		_hash_blobs.put(blob, name);
	}	
	
	public void addTree(Tree tree, String name){
		_hash_trees.put(tree, name);
	}
	
	@Override
	public String toString(){
		return buildData().toString();
	}
}