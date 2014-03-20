/******************************************************************************
 * Copyright (c) 2014. Florian Mahon <florian@faivre-et-mahon.ch>             *
 *                                                                            *
 * This file is part of backuptools.                                          *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify       *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation, either version 3 of the License, or          *
 * any later version.                                                         *
 *                                                                            *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of                 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * General Public License for more details. You should have received a        *
 * copy of the GNU General Public License along with this program.            *
 * If not, see <http://www.gnu.org/licenses/>.                                *
 ******************************************************************************/

package ch.fetm.backuptools.common.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TreeTest {
    private Tree tree;

    @Before
    public void setUp() throws Exception {
        tree = new Tree();
        TreeInfo tinfo1 = new TreeInfo();
        tinfo1.type = TreeInfo.TYPE_BLOB;
        tinfo1.SHA = "ASDFADSF";
        tinfo1.name = "Blob1";

        TreeInfo tinfo2 = new TreeInfo();
        tinfo2.type = TreeInfo.TYPE_BLOB;
        tinfo2.SHA = "ASDFADSF";
        tinfo2.name = "Blob2";

        TreeInfo tinfo3 = new TreeInfo();
        tinfo3.type = TreeInfo.TYPE_BLOB;
        tinfo3.SHA = "ASDFADSF";
        tinfo3.name = "Blob3";

        tree.addTreeInfo(tinfo1);
        tree.addTreeInfo(tinfo3);
        tree.addTreeInfo(tinfo2);

        TreeInfo tinfo01 = new TreeInfo();
        tinfo01.type = TreeInfo.TYPE_TREE;
        tinfo01.SHA = "ASDFADSF";
        tinfo01.name = "Tree1";

        TreeInfo tinfo02 = new TreeInfo();
        tinfo02.type = TreeInfo.TYPE_TREE;
        tinfo02.SHA = "ASDFADSF";
        tinfo02.name = "Tree2";

        TreeInfo tinfo03 = new TreeInfo();
        tinfo03.type = TreeInfo.TYPE_TREE;
        tinfo03.SHA = "ASDFADSF";
        tinfo03.name = "Tree3";

        tree.addTreeInfo(tinfo01);
        tree.addTreeInfo(tinfo03);
        tree.addTreeInfo(tinfo02);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void buildText() {
        StringBuilder result = new StringBuilder("blob\tASDFADSF\tBlob1\n" +
                "blob\tASDFADSF\tBlob2\n" +
                "blob\tASDFADSF\tBlob3\n" +
                "tree\tASDFADSF\tTree1\n" +
                "tree\tASDFADSF\tTree2\n" +
                "tree\tASDFADSF\tTree3\n");

        StringBuffer string = tree.buildData();


        assertEquals(result.length(), string.length());
        for (int cpt = 0; cpt < result.length(); cpt++) {
            assertEquals(string.toString().toCharArray()[cpt], result.toString().toCharArray()[cpt]);
        }
    }

    @Test
    public void testToString() {
        StringBuffer stringBuffer = tree.buildData();
        assertEquals(stringBuffer.toString(), tree.toString());
    }

    @Test
    public void getTreeInfos() {
        assertEquals(tree.getAllTreeInfo().size(), 6);
    }
}
