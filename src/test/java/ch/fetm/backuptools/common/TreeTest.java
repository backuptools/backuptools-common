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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeTest {
	private Tree tree;
	
	@Before
	public void setUp() throws Exception {
		tree = new Tree();
		Tree childTree = new Tree();
		Blob blob = new Blob("ASDFADSF");
		tree.addNode(blob, "Blob1");
		tree.addNode(blob, "Blob3");
		tree.addNode(blob, "Blob2");
		
		tree.addTree(childTree, "Tree1");
		tree.addTree(childTree, "Tree3");
		tree.addTree(childTree, "Tree2");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void buildText()
	{	
		StringBuffer result = new StringBuffer( "blob\tASDFADSF\tBlob1\n"+
									  			"blob\tASDFADSF\tBlob2\n"+
									  			"blob\tASDFADSF\tBlob3\n"+
									  			"tree\tda39a3ee5e6b4b0d3255bfef95601890afd80709\tTree1\n"+
									  			"tree\tda39a3ee5e6b4b0d3255bfef95601890afd80709\tTree2\n"+
									  			"tree\tda39a3ee5e6b4b0d3255bfef95601890afd80709\tTree3\n");
		
		StringBuffer string = tree.buildData();
		
		
		assertEquals(result.length(), string.length());
		for(int cpt = 0; cpt < result.length(); cpt++){
			assertEquals(string.toString().toCharArray()[cpt], result.toString().toCharArray()[cpt]);
		}
	}
}
