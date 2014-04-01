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

package ch.fetm.backuptools.common.tools;

import com.jcraft.jsch.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ScpClient {
    private Session session;
    private JSch jsch;
    private String host;
    private String username;
    private String password;
    private ChannelSftp sftp;

    public ScpClient(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;


        try {
            jsch = new JSch();

            initializeScpConnexion();

            connect();

        } catch (JSchException e) {
            e.printStackTrace();
        }

    }

    private void connect() {
        if (!session.isConnected()) {
            try {
                session.connect();
                sftp = (ChannelSftp) session.openChannel("sftp");
                if (sftp != null && !sftp.isConnected()) {
                    sftp.connect();
                }
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeScpConnexion() throws JSchException {
        session = jsch.getSession(this.username, this.host);
        session.setPassword(this.password);

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
    }

    public boolean isExist(String name) {
        connect();
        boolean isExist = true;
        try {
            sftp.lstat(name);
        } catch (SftpException e) {
            isExist = false;
        }
        return isExist;
    }

    public void disconnect() {
        if (sftp.isConnected())
            sftp.disconnect();
        if (session.isConnected())
            session.disconnect();
    }

    public void rmFile(String name) {
        connect();
        try {
            sftp.rm(name);
        } catch (SftpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void put(String filename, InputStream in) {
        connect();
        try {
            sftp.put(in, filename);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public InputStream get(String fullname) {
        connect();
        InputStream result = null;
        try {
            result = sftp.get(fullname);
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> ls(String path) {
        connect();
        List<ChannelSftp.LsEntry> entries;
        List<String> result = new ArrayList<>();
        try {
            entries = sftp.ls(path);
            for (ChannelSftp.LsEntry lsEntry : entries) {
                if (!lsEntry.getFilename().equals(".") && !lsEntry.getFilename().equals(".."))
                    result.add(lsEntry.getFilename());
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void rmdir(String path) {
        try {
            sftp.rm(path);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    public void createFolderTree(String directory) {
        connect();
        StringTokenizer tokenizer = new StringTokenizer(directory, "/");
        String currentDir = null;
        try {
            if (directory.startsWith("/")) {
                sftp.cd("/");
            }
            currentDir = sftp.pwd();
            while (tokenizer.hasMoreTokens()) {
                String currentToken = tokenizer.nextToken();
                if (!isExist(currentToken))
                    sftp.mkdir(currentToken);
                sftp.cd(currentToken);
            }
            sftp.cd(currentDir);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

}
