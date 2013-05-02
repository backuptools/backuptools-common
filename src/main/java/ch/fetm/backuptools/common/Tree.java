
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ch.fetm.backuptools.common.sha.SHA1;
import ch.fetm.backuptools.common.sha.SHA1Signature;


public class Tree{
	
	private HashMap<String, Blob> _hash_blobs = new HashMap<String, Blob>();
	private HashMap<String, Tree> _hash_trees = new HashMap<String, Tree>();
	
	public StringBuffer buildData(){
		StringBuffer out  = new StringBuffer();
		
		List<String> blobs = new ArrayList<String>(_hash_blobs.keySet());
		Collections.sort(blobs);
		
		List<String> trees = new ArrayList<String>(_hash_trees.keySet());
		Collections.sort(trees);
		
		for(String blobname : blobs){
			out.append("blob"+"\t"+_hash_blobs.get(blobname).getName()+"\t"+blobname+"\n");
		}
		
		for(String treename : trees){
			out.append("tree"+"\t"+_hash_trees.get(treename).getName()+"\t"+treename+"\n");
		}
		return out;
	}
	
	private String getName() {		
		SHA1 sha = new SHA1();
		SHA1Signature signature =  sha.SHA1SignStringBuffer(buildData());
		return signature.toString();
	}
	
	public void addNode(Blob blob, String name) {
		_hash_blobs.put(name, blob);
	}	
	
	public void addTree(Tree tree, String name){
		_hash_trees.put(name, tree);
	}
	
	@Override
	public String toString(){
		return buildData().toString();
	}
}