/******************************************************************************
 * Copyright (c) 2013,2014. Florian Mahon <florian@faivre-et-mahon.ch>        *
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

package ch.fetm.backuptools.common.tools;

import ch.fetm.backuptools.testingtools.ConfigFiles;
import ch.fetm.backuptools.testingtools.FileSystemTools;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.lang.System.exit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScpClientTest {
    private static String TEST_DIR = "sftp";
    private ScpClient sftp;
    private String remotedir = null;

    @Before
    public void Setup() throws Exception {
        sftp = new ScpClient(ConfigFiles.get().getSSHHost(),
                ConfigFiles.get().getSSHUser(),
                ConfigFiles.get().getSSHUserPassword());

        remotedir = ConfigFiles.get().getSSHTestDirectoryLocation() + TEST_DIR;

        if (sftp.isExist(remotedir)) {
            System.out.println("Remote directory exist be careful because it's removed after test!!!");
            System.out.println(remotedir);
            exit(-1);
        }
    }


    @Test
    public void createFolderTree() throws IOException {
        sftp.createFolderTree(remotedir + "/test1/test2");
        assertTrue(sftp.isExist(remotedir + "/test1/test2"));
    }

    @Test
    public void listFiles() {
        sftp.createFolderTree(remotedir + "/list/");
        StringBuffer sb = new StringBuffer("petit test");
        sftp.put(remotedir + "/list/file1.txt", new ByteArrayInputStream(sb.toString().getBytes()));
        sftp.put(remotedir + "/list/file2.txt", new ByteArrayInputStream(sb.toString().getBytes()));
        sftp.put(remotedir + "/list/file3.txt", new ByteArrayInputStream(sb.toString().getBytes()));

        HashMap<String, Objects> hashMap = new HashMap<>();

        List<String> list = sftp.ls(remotedir + "/list/");
        for (String s : list) {
            hashMap.put(s, null);
        }

        assertEquals(list.size(), 3);
        assertTrue(hashMap.containsKey("file1.txt"));
        assertTrue(hashMap.containsKey("file2.txt"));
        assertTrue(hashMap.containsKey("file3.txt"));
    }

    @After
    public void tearDown() {
        sftp.disconnect();
        FileSystemTools.eraseDirectory(remotedir);
    }
}
