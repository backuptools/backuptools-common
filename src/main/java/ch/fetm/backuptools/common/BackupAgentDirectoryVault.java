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

import java.nio.file.Paths;
import java.util.List;

public class BackupAgentDirectoryVault {
	private NodeDatabase database; 
	private BackupAgent  agent;
	private BackupAgentConfig config;   
	
	private void initialize(){
		database = new NodeDatabase();
		agent    = new BackupAgent(database);		
	}
	
	public BackupAgentDirectoryVault(){
		initialize();
	}
	
	public BackupAgentDirectoryVault(BackupAgentConfig config) {
		initialize();
		this.config = config;
		database.setVaultLocation(this.config.getVault_path());
	}

	public void setConfiguration(BackupAgentConfig config){
		this.config = config;
		database.setVaultLocation(this.config.getVault_path());
	}
	
	public void doBackup(){
		agent.backupDirectory(Paths.get(config.getSource_path()));
	}

	public List<Backup> getBackups(){
		return agent.getListBackups();
	}

	public void restore(Backup backup, String restore_path) {
		agent.restore(backup.getName(),restore_path);
	}

	public BackupAgentConfig getConfiguration() {
		return config;
	}
}
