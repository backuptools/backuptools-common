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

package org.fetm.backuptools.common.datanode;

import org.fetm.backuptools.common.tools.ScpClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by florian on 20.03.14.
 */
public class WORMSftpFileSystem implements IWORMFileSystem{
    private static String FOLDERSEPARATOR = "/";
    private ScpClient scpClient;
    private String directoryLocation;


    public WORMSftpFileSystem(ScpClient scpClient, String directoryLocation){
        this.scpClient = scpClient;
        this.directoryLocation = directoryLocation;
    }

    private String fullNameComposer(String name){
        String fullLocation = directoryLocation + FOLDERSEPARATOR + name;
        return  fullLocation;
    }

    @Override
    public void writeFile(String fullname, InputStream inputStream) throws IOException {
       // Check si le repertoire exit
        if (!scpClient.isExist(directoryLocation + FOLDERSEPARATOR + fullname)) {
            StringTokenizer tokenizer = new StringTokenizer(fullname, FOLDERSEPARATOR);
            String dest = "";

            for (int cpt = 0; cpt<tokenizer.countTokens(); cpt++){
                dest = dest + FOLDERSEPARATOR + tokenizer.nextToken();
            }
            scpClient.createFolderTree(fullNameComposer(dest));
        }
        scpClient.put(fullNameComposer(fullname), inputStream);
    }

    @Override
    public void deleteFile(String fullname) throws IOException {
        scpClient.rmFile(fullNameComposer(fullname));
    }

    @Override
    public InputStream readFile(String fullname) throws FileNotFoundException {
        return scpClient.get(fullNameComposer(fullname));
    }

    @Override
    public Boolean fileExist(String fullname) {
        return scpClient.isExist(fullNameComposer(fullname));
    }

    @Override
    public List<String> getListFiles(String directory) throws IOException {
        List<String>list = scpClient.ls(fullNameComposer(directory));
        List<String> result = new ArrayList<>();
        for (int cpt = 0; cpt < list.size(); cpt++) {
            result.add(FOLDERSEPARATOR + directory + FOLDERSEPARATOR + list.get(cpt));
        }
        return  result;
    }
}
