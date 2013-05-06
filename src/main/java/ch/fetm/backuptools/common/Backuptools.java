
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

import ch.fetm.backuptools.common.sha.SHA1;

public class Backuptools extends SHA1 {

	private static String root_directory;
	private static String vault_location;
	private static String root_restore;
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		root_directory   	= "/home/florian/Documents/fetm";
		vault_location      = "/home/florian/BCK";
		root_restore        = "/home/florian/REST";
		
		BackupAgent vault = new BackupAgent(new NodeDatabase(vault_location));
		vault.backupDirectory(Paths.get(root_directory));
		
		List<Backup> backups = vault.getListBackups();
	}
}
