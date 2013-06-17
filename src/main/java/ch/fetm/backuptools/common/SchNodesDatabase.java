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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SchNodesDatabase implements INodeDatabase {
	private INodeDatabase fsDatabase;
	private ScpClient scpclient;
	private String remoteVault;
	private String stagingdir;
	
	public SchNodesDatabase(Path stagingDirectory, String host, String username, String password, String remotevault){
		fsDatabase = new NodeDirectoryDatabase(stagingDirectory);
		this.stagingdir = stagingDirectory.toAbsolutePath().toString();
		scpclient = new ScpClient(host, username, password);
		this.remoteVault = remotevault;
		
		if(!Files.exists(Paths.get(stagingDirectory+FileSystems.getDefault().getSeparator()+"index.txt"))){
			scpclient.get(getRemoteFileName("index.txt"), getLocalFileName("index.txt"));
			scpclient.get(getRemoteFileName("blob.txt"), getLocalFileName("blob.txt"));
		}
	}
	
	@Override
	public void addLineToIndexFiles(String line) {
		fsDatabase.addLineToIndexFiles(line);
		scpclient.put( getLocalFileName("index.txt"), 
						getRemoteFileName("index.txt"));
	}

	@Override
	public String sendStringBuffer(StringBuffer sb) {
		String signature = fsDatabase.sendStringBuffer(sb);
		
		if(!scpclient.isExist(getRemoteFileName(signature))){
		scpclient.put(	getLocalFileName(signature), 
						getRemoteFileName(signature));
		}

		return signature;
	}

	private String getRemoteFileName(String signature) {
		return remoteVault+FileSystems.getDefault().getSeparator()+signature;
	}

	private String getLocalFileName(String signature) {
		return stagingdir+FileSystems.getDefault().getSeparator()+signature;
	}

	@Override
	public Blob sendFile(Path file) {
		Blob blob = fsDatabase.sendFile(file);
		scpclient.put(getLocalFileName("blob.txt"), getRemoteFileName("blob.txt"));
		if(!scpclient.isExist(getRemoteFileName(blob.getName()))){
			scpclient.put(	getLocalFileName(blob.getName()), 
							getRemoteFileName(blob.getName()));
		}
		
		try {
			Files.delete(Paths.get(getLocalFileName(blob.getName())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return blob;
	}

	@Override
	@SuppressWarnings("resource")
	public InputStream createInputStreamFromNodeName(String signature) {
		InputStream in;
		if(getBlobList().contains(signature)){
			scpclient.get(	getRemoteFileName(signature), 
							getLocalFileName(signature));
			in = new FileInputStreamDeleteAfterClose(Paths.get(getLocalFileName(signature)));
		}else{
			if(!Files.exists(Paths.get(getLocalFileName(signature))))
				scpclient.get(getRemoteFileName(signature), getLocalFileName(signature));
			in = fsDatabase.createInputStreamFromNodeName(signature);
		}
		return in;
	}

	@Override
	public Reader createInputStreamFromIndex() {
		return fsDatabase.createInputStreamFromIndex();
	}

	@Override
	public BlobList getBlobList() {
		return fsDatabase.getBlobList();
	}

	@Override
	public void initFS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFSInitialized() {
		// TODO Auto-generated method stub
		return false;
	}
}
