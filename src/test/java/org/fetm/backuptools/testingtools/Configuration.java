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

package org.fetm.backuptools.testingtools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

public class Configuration {

    private final String SSH_USER = "ssh.user";
    private final String SSH_HOST = "ssh.host";
    private final String SSH_PASS = "ssh.password";

    private Properties properties;

    public Configuration(Path configFile) throws IOException {
        InputStream inputStream = new FileInputStream(configFile.toFile());
        this.properties = new Properties();
        this.properties.load(inputStream);
    }


    public String getSSHUser() {
        return properties.getProperty(SSH_USER);
    }

    public String getSSHUserPassword() {
        return properties.getProperty(SSH_PASS);
    }

    public String getSSHHost() {
        return properties.getProperty(SSH_HOST);
    }

    public String getSSHTestDirectoryLocation() {
        return properties.getProperty("ssh.test.directory");
    }
}
