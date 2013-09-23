package ch.fetm.backuptools;

import java.nio.file.Path;
import java.nio.file.Paths;

import ch.fetm.backuptools.common.BackupAgent;
import ch.fetm.backuptools.common.datanode.NodeDirectoryDatabase;

public final class Bckcmd {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if(args[1].equalsIgnoreCase("bck")){	
				String source = args[2];
				String dest   = args[3];
				if(args.length == 4){System.exit(-1);};
				backupDirectoryToVault(source, dest);
			}
		} catch (Exception e) {
			printHelp();
		}
	}

	private static void printHelp() {
		System.out.println("bckcmd bck [source] [vault]");
	}

	private static void backupDirectoryToVault(String source, String dest) {
		NodeDirectoryDatabase ndb = new NodeDirectoryDatabase();
		
		Path pDest = Paths.get(dest);
		if(!pDest.toFile().exists() || !pDest.toFile().isDirectory()){
			System.exit(-1);
		}
		
		Path pSource = Paths.get(source);
		if(pSource.toFile().exists()){
			System.exit(-1);
		}
		
		ndb.setVaultLocation(pDest);
		BackupAgent ba = new BackupAgent(ndb);
		ba.backupDirectory(pSource);
	}

}
