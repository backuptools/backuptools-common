package ch.fetm.backuptools.testingtools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Created by fmn on 29.03.14.
 */
public class Configuration {

    private final String SSH_USER = "ssh.user";
    private final String SSH_HOST = "ssh.host";
    private final String SSH_PASS = "ssh.password";


    private Path configfile;
    private Properties properties;

    public Configuration(Path configFile){
        try {
            InputStream inputStream = new FileInputStream(configFile.toFile());
            this.configfile = configFile;
            this.properties = new Properties();
            this.properties.load(inputStream);
        } catch (IOException e) {

        }
    }


    public String getSSHUser(){
        return properties.getProperty(SSH_USER);
    }

    public String getSSHUserPassword(){
        return properties.getProperty(SSH_PASS);
    }

    public String getSSHHost(){
        return properties.getProperty(SSH_HOST);
    }

    public String getSSHTestDirectoryLocation(){
        return properties.getProperty("ssh.test.directory");
    }
}
