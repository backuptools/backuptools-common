
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

import java.util.Date;

import javax.swing.text.DateFormatter;

public class Backup {
	private String date;
	private String info;
	private String sha_root_tree;
	

	public Backup(String date, String sha1) {
		this.date = date;
		sha_root_tree = sha1;
	}

	public String getDate(){
		return date;
	}
	
	public String getName(){
		return sha_root_tree;
	}
}
