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

import java.io.Serializable;

public class BackupAgentConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6328634670433771680L;
	
	private String source_path;
	private String vault_path;

	public void setSource_path(String path){
		source_path = path;
	}
	
	public String getSource_path(){
		return source_path;
	}
	
	public void setVault_path(String path){
		vault_path = path;
	}
	
	public String getVault_path(){
		return vault_path;
	}
}
