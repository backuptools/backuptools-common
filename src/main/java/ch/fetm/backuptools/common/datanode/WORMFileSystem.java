/*	Copyright 2014 Florian Mahon <florian@faivre-et-mahon.ch>
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

package ch.fetm.backuptools.common.datanode;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class WORMFileSystem implements IWORMFileSystem{
    private Path location;

    public static final String DIRECTORY_SEPARATOR = "/";

    public WORMFileSystem(String location){
        this.location = Paths.get(location);
    }

    public WORMFileSystem(Path location){
        this.location = location;
    }

    @Override
    public void writeFile(String fullname, InputStream inputStream) throws IOException {
        String system_fullname = location.toString()+FileSystems.getDefault().getSeparator()+convertNameToFsStyle(fullname);

        Path path = Paths.get(system_fullname);
        if (!path.toFile().exists()){
            Files.createDirectories(path.getParent());
        }
        Files.copy(inputStream, path);
    }

    private String convertNameToFsStyle(String fullname) {
        String stringpath = "";
        StringTokenizer tokenizer = new StringTokenizer(fullname,DIRECTORY_SEPARATOR);
        while(tokenizer.hasMoreTokens()){
            stringpath += (FileSystems.getDefault().getSeparator() + tokenizer.nextToken());
        }
        return stringpath;
    }

    @Override
    public void deleteFile(String fullname) throws IOException {
        String newname = convertNameToFsStyle(fullname);
        Files.delete(Paths.get(location.toString()+FileSystems.getDefault().getSeparator()+newname));
    }

    @Override
    public InputStream readFile(String fullname) throws FileNotFoundException {
        String newname = convertNameToFsStyle(fullname);
        Path file = Paths.get(location.toString()+FileSystems.getDefault().getSeparator()+newname);
        return new FileInputStream(file.toFile());
    }

    @Override
    public Boolean fileExist(String fullname) {
        String newname = convertNameToFsStyle(fullname);
        Path file = Paths.get(location.toString()+FileSystems.getDefault().getSeparator()+newname);
        return file.toFile().exists();
    }

    @Override
    public List<String> getListFiles(String directory) throws IOException {
        List<String> list = new ArrayList<>();
        String newpath = convertNameToFsStyle(directory);
        DirectoryStream<Path> directories = Files.newDirectoryStream(Paths.get(location.toString() + FileSystems.getDefault().getSeparator() + newpath));

        for(Path file : directories){
            list.add(DIRECTORY_SEPARATOR+directory+DIRECTORY_SEPARATOR+file.getFileName());
        }
        return list;
    }
}
