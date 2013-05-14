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
