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

package ch.fetm.backuptools.common.model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import ch.fetm.backuptools.common.tools.SHA1;
import ch.fetm.backuptools.common.tools.SHA1Signature;


public class Tree{
	
	private HashMap<String, TreeInfo> _hash_blobs = new HashMap<String, TreeInfo>();
	private HashMap<String, TreeInfo> _hash_trees = new HashMap<String, TreeInfo>();
	
	public StringBuffer buildData(){
		StringBuffer out  = new StringBuffer();
		
		List<String> blobs = new ArrayList<String>(_hash_blobs.keySet());
		Collections.sort(blobs);
		
		List<String> trees = new ArrayList<String>(_hash_trees.keySet());
		Collections.sort(trees);
		
		for(String blobname : blobs){
			out.append(TreeInfo.TYPE_BLOB+"\t"+_hash_blobs.get(blobname).SHA+"\t"+blobname+"\n");
		}
		
		for(String treename : trees){
			out.append(TreeInfo.TYPE_TREE+"\t"+_hash_trees.get(treename).SHA+"\t"+treename+"\n");
		}
		return out;
	}
	
	public String getName() {		
		SHA1 sha = new SHA1();
		SHA1Signature signature =  sha.SHA1SignStringBuffer(buildData());
		return signature.toString();
	}
	
	public void addTreeInfo(TreeInfo treeInfo) {
		if(treeInfo.type.equals(TreeInfo.TYPE_BLOB))
			_hash_blobs.put(treeInfo.name, treeInfo);
		else
			_hash_trees.put(treeInfo.name, treeInfo);
					
	}
	
	@Override
	public String toString(){
		return buildData().toString();
	}
	
	public Tree(){
		
	}
	
	public Tree(InputStream is){
		List<TreeInfo> trees = new ArrayList<TreeInfo>();
		String line;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		try {
			while((line = reader.readLine()) != null){
				StringTokenizer tokenizer = new StringTokenizer(line,"\t");
				TreeInfo tree = new TreeInfo();
				tree.type = tokenizer.nextToken();
				tree.SHA  = tokenizer.nextToken();
				tree.name = tokenizer.nextToken();
				addTreeInfo(tree);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<TreeInfo> getAllTreeInfo() {
		List<TreeInfo> treeinfos = new ArrayList<TreeInfo>();
		treeinfos.addAll(_hash_blobs.values());
		treeinfos.addAll(_hash_trees.values());
		return treeinfos;
	}
}