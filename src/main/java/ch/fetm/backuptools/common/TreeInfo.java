package ch.fetm.backuptools.common;

public class TreeInfo {
	public static String TYPE_TREE = "tree";
	public static String TYPE_BLOB = "blob";
	public String SHA;
	public String type;
	public String name;
	
	@Override
	public String toString() {
		return name;
	}
}
