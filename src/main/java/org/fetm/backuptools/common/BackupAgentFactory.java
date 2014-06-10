/******************************************************************************
 * Copyright (c) 2013,2014. Florian Mahon <florian@faivre-et-mahon.ch>        *
 *                                                                            *
 * This file is part of backuptools.                                          *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify       *
 * it under the terms of the GNU General Public License as published by       *
 * the Free Software Foundation, either version 3 of the License, or          *
 * any later version.                                                         *
 *                                                                            *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of                 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * General Public License for more details. You should have received a        *
 * copy of the GNU General Public License along with this program.            *
 * If not, see <http://www.gnu.org/licenses/>.                                *
 ******************************************************************************/

package org.fetm.backuptools.common;

import org.fetm.backuptools.common.datanode.*;
import org.fetm.backuptools.common.tools.ScpClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BackupAgentFactory {
    public static BackupAgent buildBackupAgent(String file) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        VaultConfigPersistance config_persistance = new VaultConfigPersistance(properties);

        VaultConfiguration configuration = config_persistance.read();

        return buildBackupAgent(configuration);
    }

    public static BackupAgent buildBackupAgent(VaultConfiguration configuration) {
        IWORMFileSystem fs = null;
        if (configuration.getVaultType().equals(VaultConfiguration.TYPE_SFTP)) {
            ScpClient scp = new ScpClient(configuration.getHost(),
                    configuration.getUser(),
                    configuration.getPass());

            fs = new WORMSftpFileSystem(scp, configuration.getLocation());
        } else if (configuration.getVaultType().equals(VaultConfiguration.TYPE_DIR)) {
            fs = new WORMFileSystem(configuration.getLocation());
        }


        INodeDatabase db = new NodeDirectoryDatabase(fs);
        return new BackupAgent(Paths.get(configuration.getDirectory()), db);
    }

    public static List<IBackupAgent> buildBackupAgents(Path directory) throws IOException {
        DirectoryStream<Path> files = Files.newDirectoryStream(directory, "*.properties");
        List<IBackupAgent> agents = new ArrayList<>();
        for(Path file : files){
            agents.add(BackupAgentFactory.buildBackupAgent(file.toString()));
        }
        return agents;
    }

}