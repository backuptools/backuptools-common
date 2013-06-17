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

import java.net.ConnectException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

public class ScpClient {
	private Session session;
	private JSch jsch;
	private String host;
	private String username;
	private String password;
	private ChannelSftp sftp;
	
	public ScpClient(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
		
		
		try {
			jsch = new JSch();

		    initializeScpConnexion();
		    
		    connect();

		} catch (JSchException e) {
			e.printStackTrace();
		}
		
	}

	private void connect() { 
		if(!session.isConnected()){
			try {
				session.connect();
				sftp = (ChannelSftp) session.openChannel("sftp");
				if(sftp != null || !sftp.isConnected()){
					sftp.connect();
				}
			} catch (JSchException e) {
				e.printStackTrace();
			}
		}
	}

	private void initializeScpConnexion() throws JSchException {
		session = jsch.getSession(this.username, this.host);
		session.setPassword(this.password);
				    
		java.util.Properties config = new java.util.Properties(); 
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
	}

	public boolean isExist(String name) {
		connect();
		boolean isExist = true;
		try {
			sftp.lstat(name);
		} catch (SftpException e) {
			isExist = false;
		}
		return isExist;
		
	}

	public void rmFile(String name) {
		connect();
		try {
			sftp.rm(name);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void get(String src, String dst) {
		connect();
		try {
			sftp.get(src, dst);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	public void put(String localname, String dest) {
		connect();
		try {
			sftp.put(localname, dest);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

}