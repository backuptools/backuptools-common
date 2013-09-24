package ch.fetm.backuptools.common;

import ch.fetm.backuptools.common.datanode.INodeDatabase;
import ch.fetm.backuptools.common.datanode.NodeDirectoryDatabase;

public class BackupAgentFactory {

	public static IBackupAgent create(BackupAgentConfig config) {
		INodeDatabase db = new NodeDirectoryDatabase(config.getVault_path());
		IBackupAgent agent = new BackupAgent(config.getSource_path(),db);
		return agent;
	}

}
