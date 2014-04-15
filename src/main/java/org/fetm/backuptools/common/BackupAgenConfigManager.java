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

package org.fetm.backuptools.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


public abstract class BackupAgenConfigManager {

	public final static void writeConfigurationFile(BackupAgentConfig config) {
		Properties properties = new Properties();
		Path configfile = getConfigFile();
		
		if(Files.exists(configfile)){
			try {
				Files.delete(configfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		properties.setProperty("source", config.getSource_path().toAbsolutePath().toString());
		properties.setProperty("vault", config.getVault_path().toAbsolutePath().toString());
		
		try {
			OutputStream out = new FileOutputStream(configfile.toFile());
			properties.store(out , "--- No comments---");
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static BackupAgentConfig readConfigurationInFile() {
		BackupAgentConfig config = new BackupAgentConfig();
		Properties properties = new Properties();
		Path configfile = getConfigFile();
		try {
			InputStream in = new FileInputStream(configfile.toFile());
			properties.load(in);
			config.setSource_path(Paths.get(properties.getProperty("source")));
			config.setVault_path(Paths.get(properties.getProperty("vault")));
			
			in.close();
		} catch (IOException e) {
			config = null;
		}
		return config;	
    }

	private static Path getConfigFile() {
		String home = System.getProperty("user.home");
		
    	Path path = Paths.get( home
    						   + FileSystems.getDefault().getSeparator() 
    						   + ".backuptools");
    	
    	if(!path.toFile().exists()){
    		try {
				Files.createDirectory(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	Path configfile = Paths.get( path.toAbsolutePath().toString() 
    								 + FileSystems.getDefault().getSeparator()
    								 + "config.properties");
   
		return configfile;
	}
}
