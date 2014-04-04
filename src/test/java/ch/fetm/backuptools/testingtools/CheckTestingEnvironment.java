
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

package ch.fetm.backuptools.testingtools;

import ch.fetm.backuptools.common.tools.ScpClient;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class CheckTestingEnvironment {

    private Configuration configuration = null;
    private ScpClient scpClient = null;

    @Before
    public void setup() {
        configuration = ConfigFiles.get();
        scpClient = new ScpClient(configuration.getSSHHost(),
                configuration.getSSHUser(),
                configuration.getSSHUserPassword());

    }

    @Test
    public void testConfigFileForTest() {
        assertNotNull("You don't have a config file, please must create it \n" +
                        "(./backuptools/test_config.properties)\n " +
                        "See an example in the resources directory",
                configuration
        );
    }


    @Test
    public void testIfSSHConnexionIsLocal(){
        List<String> result = scpClient.ls("/");
        assertNotNull("The sftp connexion error", result);
    }

    @Test
    public void testIfDirectoryDestinationExist() {
        assertTrue("The destination directory don't exit please create it",
                scpClient.isExist(ConfigFiles.get().getSSHTestDirectoryLocation()));
    }
}
