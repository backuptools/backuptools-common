package ch.fetm.backuptools.testingtools;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by fmn on 29.03.14.
 */
public class ConfigFiles {
    private static String configuration_directory = "backuptools";
    private static Configuration configuration = null;


    static Configuration get(){
        if (configuration == null) {
            configuration = createConfigurationInstance();
        }
        return configuration;
    }

    private static Configuration createConfigurationInstance() {
        Configuration result = null;
        String configfile = System.getProperty("user.home")
                + FileSystems.getDefault().getSeparator()
                + configuration_directory;

        Path pConfigFile = Paths.get(configfile);
        if(!pConfigFile.toFile().exists()){
            result = new Configuration(pConfigFile);
        }
        return result;
    }
}
