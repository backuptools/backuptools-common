package ch.fetm.backuptools.common;

import java.util.List;

import ch.fetm.backuptools.common.datanode.INodeDatabase;
import ch.fetm.backuptools.common.model.Backup;
import ch.fetm.backuptools.common.model.Tree;
import ch.fetm.backuptools.common.model.TreeInfo;

public interface IBackupAgent {

	List<Backup> getListBackups();

	void setDatabase(INodeDatabase data);

	void restore(List<TreeInfo> trees, String restore_path);

	Tree getTreeInfosOf(String sha);

	void doBackup();

}
