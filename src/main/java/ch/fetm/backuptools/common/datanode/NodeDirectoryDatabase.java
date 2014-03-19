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

import ch.fetm.backuptools.common.model.Backup;
import ch.fetm.backuptools.common.model.Blob;
import ch.fetm.backuptools.common.model.Tree;
import ch.fetm.backuptools.common.tools.SHA1;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NodeDirectoryDatabase implements INodeDatabase {

	private static String BLOBS_DIRECTORY = "blobs";
	private static String TREES_DIRECTORY = "trees";
    private static String BACKUP_DIRECTORY = "backups";
	private static int LAST_DIRECTORY_SUBSTRING_BEGIN_INDEX = 0;
	private static int LAST_DIRECTORY_SUBSTRING_END_INDEX   = 2;
    IWORMFileSystem fileSystem;

    public NodeDirectoryDatabase(WORMFileSystem fs) {
        fileSystem = fs;
    }

    public NodeDirectoryDatabase() {
    }

    private String sendStringBuffer(String location, StringBuffer sb) {
        String sign = SHA1.SHA1SignStringBuffer(sb);
        InputStream input = new ByteArrayInputStream(sb.toString().getBytes());

        String fullPath;
        try {
            fullPath = createFullPath(location, sign);
            String fullPathName = fullPath + "/" + sign;

            if (!fileSystem.fileExist(fullPathName)) {
                fileSystem.writeFile(fullPathName, input);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return sign;
    }

    private String createFullPath(String subdir, String SHA) throws IOException {
        String hashdir;
        hashdir = SHA.substring(LAST_DIRECTORY_SUBSTRING_BEGIN_INDEX,
                LAST_DIRECTORY_SUBSTRING_END_INDEX);

        return FileSystems.getDefault().getSeparator()
                + subdir
                + FileSystems.getDefault().getSeparator()
                + hashdir;
    }

    @Override
    public void sendTree(Tree tree) {
        StringBuffer stringBuffer = tree.buildData();
        sendStringBuffer(TREES_DIRECTORY,stringBuffer);
    }

    @Override
	public Blob sendFile(Path file){
		Blob blob = null;
        String blobName = SHA1.SHA1SignFile(file);
        String blobFullPath;
        try {
            InputStream inputStream = new FileInputStream(file.toFile());
            blobFullPath = createFullPath(BLOBS_DIRECTORY, blobName)+FileSystems.getDefault().getSeparator()+blobName;
			if(! fileSystem.fileExist(blobFullPath)){
                fileSystem.writeFile(blobFullPath, inputStream);
				inputStream.close();
			}
			blob = new Blob(blobName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return blob;
	}

	@Override
	public InputStream createInputStreamFromNodeName(String signature){
		InputStream inputStream;
		String fullPathBlob;
		String fullPathTree;

		try {
			fullPathBlob = createFullPath(BLOBS_DIRECTORY, signature)+'/'+signature;
			fullPathTree = createFullPath(TREES_DIRECTORY, signature)+'/'+signature;

			if(fileSystem.fileExist(fullPathBlob)){
				inputStream = fileSystem.readFile(fullPathBlob);
			} else if(fileSystem.fileExist(fullPathTree)){
				inputStream = fileSystem.readFile(fullPathTree);
			} else {
				return null;
			}

			return inputStream;

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

    @Override
    public void sendBackup(Backup backup) {
        StringBuffer stringBuffer = backup.buildData();
        sendStringBuffer(BACKUP_DIRECTORY, stringBuffer);
    }

    @Override
    public List<Backup> getBackups() throws IOException {
        List<Backup> backups = new ArrayList<>();
        List<String> listDirectories = fileSystem.getListFiles("/backups");
        for(String directory : listDirectories){
            List<String> files = fileSystem.getListFiles(directory);
            for(String file : files){
                InputStream in = fileSystem.readFile(file);
                backups.add(new Backup(in));
                in.close();
            }
        }
        return backups;
    }
}
