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

import ch.fetm.backuptools.common.TestUtilities;
import ch.fetm.backuptools.common.model.Backup;
import ch.fetm.backuptools.common.model.Tree;
import ch.fetm.backuptools.common.model.TreeInfo;
import ch.fetm.backuptools.common.tools.SHA1;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class NodeDirectoryDatabaseTest {
	private INodeDatabase database;
	private Path db_location;
	
	@Before
	public void setup(){
		db_location = TestUtilities.createTemporyFile();
		WORMFileSystem fs = new WORMFileSystem(db_location);
		database = new NodeDirectoryDatabase(fs);
	}


	
	@After
	public void tearDown(){
		
	}

    @Test
    public void sendBackupAndGetBackups() {
        HashMap<String, Backup> hashMap = new HashMap<>();
        Backup bck1 = new Backup("date", "SHA1");
        Backup bck2 = new Backup("date", "SHA2");

        database.sendBackup(bck1);
        String sha1 = SHA1.SHA1SignStringBuffer(bck1.buildData());
        hashMap.put(sha1, bck1);

        database.sendBackup(bck2);
        String sha2 = SHA1.SHA1SignStringBuffer(bck2.buildData());
        hashMap.put(sha2, bck2);

        List<Backup> backups = null;

        try {
            backups = database.getBackups();

            for (Backup bck : backups) {
                String shastring = SHA1.SHA1SignStringBuffer(bck.buildData());
                assertTrue(hashMap.containsKey(shastring));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        assertEquals(backups.size(), 2);
    }

    @Test
    public void sendTree(){
        Tree tree = new Tree();
        TreeInfo treeInfo = new TreeInfo();
        treeInfo.name = "name";
        treeInfo.SHA  = "SHA123123";
        treeInfo.type = TreeInfo.TYPE_BLOB;

        tree.addTreeInfo(treeInfo);

        database.sendTree(tree);

        String name = tree.getName();
        InputStream inputStream = database.createInputStreamFromNodeName(name);
        Tree tree1 = new Tree(inputStream);
        assertTrue(tree.getName().equals(tree1.getName()));
    }
	@Test
	public void sendFile_createInputStreamFromNodeName(){
		String filecontents = "Hello world and I test you";
		String filename = "file.txt";
		
		Path file = Paths.get( db_location.toAbsolutePath().toString()
								+FileSystems.getDefault().getSeparator()
								+filename);

		OutputStream out;
		try {
			out = new FileOutputStream(file.toFile());
			out.write(filecontents.getBytes());
			out.close();

            String blobname = SHA1.SHA1SignFile(file);

            database.sendFile(file);

            BufferedReader in = new BufferedReader(new InputStreamReader(database.createInputStreamFromNodeName(blobname)));
			
			assertEquals(filecontents, in.readLine());
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertFalse(true);
		}
		

	}

}
