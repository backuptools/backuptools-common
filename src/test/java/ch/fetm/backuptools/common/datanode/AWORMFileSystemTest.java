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

package ch.fetm.backuptools.common.datanode;

import ch.fetm.backuptools.common.tools.SHA1;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by florian on 20.03.14.
 */
public abstract class AWORMFileSystemTest {
    private String file = "/test/mydocuments";
    private StringBuffer sb = new StringBuffer();
    protected IWORMFileSystem worm;
    @Before
    public void setUp() throws Exception{
        sb.append("Litle test for writing a line in wormfs");
    }

    @After
    public abstract void tearDown() throws Exception;

    @Test
    public void testReadWriteFile() throws Exception {
        InputStream source = new ByteArrayInputStream(sb.toString().getBytes());
        worm.writeFile(file, source);
        source.close();

        source = new ByteArrayInputStream(sb.toString().getBytes());
        String signature_2 = SHA1.SHA1SignInputStream(source);
        source.close();

        InputStream sources_fs =  worm.readFile(file);
        String signature_1 = SHA1.SHA1SignInputStream(sources_fs);
        sources_fs.close();

        assertTrue(signature_1.equals(signature_2));
        assertFalse(signature_1.equals(SHA1.SHA1SignStringBuffer(new StringBuffer())));

    }

    @Test
    public void testListFilesInDirectory(){
        StringBuffer sb = new StringBuffer();
        List<String> listfiles = new ArrayList<>();
        sb.append("test");
        int nbFile = 10;
        String dir = "directory";
        for(int cpt = 0; cpt < nbFile; cpt++){
            InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
            try {
                String name = dir+WORMFileSystem.DIRECTORY_SEPARATOR+"file"+cpt;
                listfiles.add(name);
                worm.writeFile(name,in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<String> list = null;
        try {
            list = worm.getListFiles(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(list.size(), nbFile);
    }

    @Test
    public void testDeleteExistFile() throws Exception {
        InputStream source = new ByteArrayInputStream(sb.toString().getBytes());
        worm.writeFile(file, source);
        assertTrue(worm.fileExist(file));
        worm.deleteFile(file);
        assertFalse(worm.fileExist(file));
    }
}
