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

package ch.fetm.backuptools.common.datanode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclFileAttributeView;
import java.security.acl.AclEntry;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import ch.fetm.backuptools.common.model.Blob;
import ch.fetm.backuptools.common.tools.SHA1;
import ch.fetm.backuptools.common.tools.SHA1Signature;

public class NodeDirectoryDatabase implements INodeDatabase {

	private static String BLOBS_DIRECTORY = "blobs";
	private static String TREES_DIRECTORY = "trees";
	
	private static int LAST_DIRECTORY_SUBSTRING_BEGIN_INDEX = 0;
	private static int LAST_DIRECTORY_SUBSTRING_END_INDEX   = 2;
	
	private Path _vault_location;
	
	public NodeDirectoryDatabase(Path vault_location) {
		setVaultLocation(vault_location);
	}
	
	public NodeDirectoryDatabase() {
		_vault_location = null;
	}

	/* (non-Javadoc)
	 * @see ch.fetm.backuptools.common.INodeDatabase#addLineToIndexFiles(java.lang.String)
	 */
	@Override
	public void addLineToIndexFiles(String line){
		FileOutputStream out = null;
		Path file = Paths.get(getIndexFileName());
		try {
			out = new FileOutputStream(file.toFile(),true);
		} catch (FileNotFoundException e1) {
			try {
				Files.createFile(file);
				out = new FileOutputStream(file.toFile(),true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			out.write(line.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.fetm.backuptools.common.INodeDatabase#sendStringBuffer(java.lang.StringBuffer)
	 */
	@Override
	public String sendStringBuffer(StringBuffer sb) {
		SHA1 sha = new SHA1();
		SHA1Signature sign = sha.SHA1SignStringBuffer(sb);

		Path finaldir;
		try {
			finaldir = buildFinalDirectory(TREES_DIRECTORY,sign.toString());
			Path file = Paths.get(finaldir+FileSystems.getDefault().getSeparator()+sign.toString());
			
			if(! Files.exists(file)) {
				FileOutputStream out = new FileOutputStream(file.toFile());
				GZIPOutputStream gzip = new GZIPOutputStream(out);
				gzip.write(sb.toString().getBytes());
				gzip.close();
				out.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		return sign.toString();
	}

	private Path buildFinalDirectory(String subdir, String SHA) throws IOException {
		String hashdir = SHA.toString().substring( LAST_DIRECTORY_SUBSTRING_BEGIN_INDEX, 
						                            LAST_DIRECTORY_SUBSTRING_END_INDEX);
		
		Path final_path = Paths.get( _vault_location+FileSystems.getDefault().getSeparator()
									 +subdir
									 +FileSystems.getDefault().getSeparator()
									 +hashdir);
		
		if(!Files.exists(final_path))
			Files.createDirectory(final_path);
		
		return final_path;
	}

	/* (non-Javadoc)
	 * @see ch.fetm.backuptools.common.INodeDatabase#sendFile(java.nio.file.Path)
	 */
	@Override
	public Blob sendFile(Path file){
		SHA1 sha  = new SHA1();
		Blob blob = null;
		String blobName =sha.SHA1SignFile(file).toString();
		Path blobfile;
		try {
			blobfile = Paths.get(buildFinalDirectory(BLOBS_DIRECTORY, blobName)+FileSystems.getDefault().getSeparator()+blobName);
			if(! Files.exists(blobfile)) {
				FileOutputStream out = new FileOutputStream(blobfile.toFile());
				GZIPOutputStream gzip = new GZIPOutputStream(out);
				Files.copy(file, gzip);
				gzip.close();
				out.close();
			}	
			blob = new Blob(blobName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return blob;
	}
	
	/* (non-Javadoc)
	 * @see ch.fetm.backuptools.common.INodeDatabase#createInputStreamFromNodeName(java.lang.String)
	 */
	@Override
	public InputStream createInputStreamFromNodeName(String signature){
		File file = null;
		Path finaldir_blob;
		Path finaldir_tree;
		
		try {
			finaldir_blob = Paths.get(buildFinalDirectory(BLOBS_DIRECTORY, signature)+FileSystems.getDefault().getSeparator()+signature);
			finaldir_tree = Paths.get(buildFinalDirectory(TREES_DIRECTORY, signature)+FileSystems.getDefault().getSeparator()+signature);
		
			if(Files.exists(finaldir_blob)){
				file = finaldir_blob.toFile();
			} else if(Files.exists(finaldir_tree)){
				file = finaldir_tree.toFile();
			}else{
				return null;
			}
			
			return new GZIPInputStream(new FileInputStream(file));
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ch.fetm.backuptools.common.INodeDatabase#createInputStreamFromIndex()
	 */
	@Override
	public Reader createInputStreamFromIndex() {
		try {
			return new FileReader(getIndexFileName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getIndexFileName() {
		return _vault_location+FileSystems.getDefault().getSeparator()+"index.txt";
	}

	public Path getVaultLocation() {
		return	_vault_location;
	}

	public void setVaultLocation(Path databaseLocation) {
		_vault_location = databaseLocation;

		if(!isFSInitialized())
			initFS();
	}

	@Override
	public void initFS() {
		Path indexbackup = Paths.get(getIndexFileName());
		Path blob_location = Paths.get(_vault_location+FileSystems.getDefault().getSeparator()+BLOBS_DIRECTORY);
		Path tree_location = Paths.get(_vault_location+FileSystems.getDefault().getSeparator()+TREES_DIRECTORY);

		try {
			Files.createFile(indexbackup);
			Files.createDirectories(blob_location);
			Files.createDirectories(tree_location);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isFSInitialized() {
		Path path = Paths.get(getIndexFileName());
		return Files.exists(path);
	}

}
