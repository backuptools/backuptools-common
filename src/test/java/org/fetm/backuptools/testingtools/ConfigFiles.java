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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigFiles {
    private static String configuration_directory = "backuptools";
    private static String configuration_file = "test_config.properties";
    private static Configuration configuration = null;


    public static Configuration get() {
        if (configuration == null) {
            try {
                configuration = createConfigurationInstance();
            } catch (IOException e) {
                configuration = null;
            }
        }
        return configuration;
    }

    private static Configuration createConfigurationInstance() throws IOException {
        Configuration result = null;
        String configuration = System.getProperty("user.home")
                + FileSystems.getDefault().getSeparator()
                + configuration_directory
                + FileSystems.getDefault().getSeparator()
                + configuration_file;

        Path pConfigFile = Paths.get(configuration);
        if (pConfigFile.toFile().exists()) {
            result = new Configuration(pConfigFile);
        } else {
            System.out.println("Config file not present! Please create it and restart test");
            System.exit(-1);
        }
        return result;
    }
}
