package ch.fetm.backuptools.common.datanode;

import ch.fetm.backuptools.common.tools.SHA1;
import ch.fetm.backuptools.common.tools.SHA1Signature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class WORMFileSystemTest {
    IWORMFileSystem worm;
    String file = "/test/mydocuments";
    StringBuffer sb = new StringBuffer();
    SHA1 sha = new SHA1();

    @Before
    public void setUp() throws Exception {
        worm = new WORMFileSystem(Files.createTempDirectory("worm"));
        sb.append("Litle test for writing a line in wormfs");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReadWriteFile() throws Exception {
        InputStream source = new ByteArrayInputStream(sb.toString().getBytes());
        worm.writeFile(file, source);
        source.close();

        source = new ByteArrayInputStream(sb.toString().getBytes());
        SHA1Signature signature_2 = sha.SHA1SignInputStream(source);
        source.close();

        InputStream sources_fs =  worm.readFile(file);
        SHA1Signature signature_1 = sha.SHA1SignInputStream(sources_fs);
        sources_fs.close();

        assertTrue(signature_1.toString().equals(signature_2.toString()));
        assertFalse(signature_1.toString().equals(sha.SHA1SignStringBuffer(new StringBuffer()).toString()));

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
