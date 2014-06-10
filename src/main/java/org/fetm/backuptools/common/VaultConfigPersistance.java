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

import java.util.Properties;

/**
 * Created by florian on 09.06.14.
 */
public class VaultConfigPersistance {
    public static final String VAULT_TYPE = "vault.type";
    public static final String VAULT_DIRECTORY = "vault.directory";
    public static final String SRC = "src";
    public static final String VAULT_SSH_USER = "vault.ssh.user";
    public static final String VAULT_SSH_HOST = "vault.ssh.host";
    public static final String VAULT_SSH_PASSWORD = "vault.ssh.password";
    public static final String VAULT_NAME = "name";

    private Properties properties;

    public VaultConfigPersistance(Properties properties){
        this.properties = properties;
    }

    public void write(VaultConfiguration configuration){
        properties.setProperty(SRC,               configuration.getDirectory());
        properties.setProperty(VAULT_DIRECTORY,   configuration.getLocation());
        properties.setProperty(VAULT_TYPE,        configuration.getVaultType());
        properties.setProperty(VAULT_SSH_USER,    configuration.getUser());
        properties.setProperty(VAULT_SSH_HOST,    configuration.getHost());
        properties.setProperty(VAULT_SSH_PASSWORD,configuration.getPass());
        properties.setProperty(VAULT_NAME,        configuration.getName());
    }

    public VaultConfiguration read(){
        VaultConfiguration configuration  = new VaultConfiguration();

        configuration.setDirectory(properties.getProperty(SRC));
        configuration.setLocation(properties.getProperty(VAULT_DIRECTORY));
        configuration.setVaultType(properties.getProperty(VAULT_TYPE));
        configuration.setUser(properties.getProperty(VAULT_SSH_USER));
        configuration.setHost(properties.getProperty(VAULT_SSH_HOST));
        configuration.setPass(properties.getProperty(VAULT_SSH_PASSWORD));
        configuration.setName(properties.getProperty(VAULT_NAME,"no name"));
        return configuration;
    }
}
