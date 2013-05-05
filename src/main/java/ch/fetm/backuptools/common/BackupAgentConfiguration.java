package ch.fetm.backuptools.common;

public class BackupAgentConfiguration {
	private String databaseLocation;
	private String sourceLocation;
	
	public void setDatabaseLocation(String location){
		databaseLocation = location;
	}
	
	public String getDatabaseLocation(){
		return databaseLocation;
	}
	
	public void setSourceLocation(String location){
		sourceLocation = location;
	}
	
	public String getSourcelocation(){
		return sourceLocation;
	}
}
