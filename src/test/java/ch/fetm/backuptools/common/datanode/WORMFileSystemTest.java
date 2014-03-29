/******************************************************************************
 * Copyright (c) 2014. Florian Mahon <florian@faivre-et-mahon.ch>             *
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

package ch.fetm.backuptools.common.datanode;

import ch.fetm.backuptools.testingtools.FileSystemTools;
import org.junit.After;
import org.junit.Before;

import java.nio.file.Files;
import java.nio.file.Path;


public class WORMFileSystemTest extends AWORMFileSystemTest {
    Path path;
    @Override
    @Before
    public void setUp() throws Exception {
        path = Files.createTempDirectory("worm");
        super.setUp();
        worm = new WORMFileSystem(path);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        FileSystemTools.eraseDirectory(path.toString());
    }

}
