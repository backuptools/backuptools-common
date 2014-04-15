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

package org.fetm.backuptools.common.datanode;

import org.fetm.backuptools.common.tools.ScpClient;
import org.fetm.backuptools.testingtools.ConfigFiles;
import org.fetm.backuptools.testingtools.FileSystemTools;

/**
 * Created by florian on 20.03.14.
 */
public class WORMSftpTest extends AWORMFileSystemTest {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        ScpClient scpClient = new ScpClient(ConfigFiles.get().getSSHHost(),
                ConfigFiles.get().getSSHUser(),
                ConfigFiles.get().getSSHUserPassword());
        worm = new WORMSftpFileSystem(scpClient,
                ConfigFiles.get().getSSHTestDirectoryLocation());

    }

    @Override
    public void tearDown() throws Exception {
        FileSystemTools.eraseDirectory(ConfigFiles.get().getSSHTestDirectoryLocation());
    }
}
