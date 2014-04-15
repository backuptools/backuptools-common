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

package org.fetm.backuptools.common.tools;

import org.junit.Test;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;


public class SHA1Test {
    @Test
    public void SHA1SignInputStream() {
        String signature = null;

        try {
            InputStream f = getClass().getClassLoader().getResourceAsStream("file.txt");
            signature = SHA1.SHA1SignInputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }


        assertEquals(signature, "f874afa73be7027485fd3e99e6e729c38ba9aeee");
    }

    @Test
    public void SHA1SignFile() {
        try {
            Path file = Paths.get(getClass().getClassLoader().getResource("file.txt").toURI());
            String signature = SHA1.SHA1SignFile(file);
            assertEquals(signature, "f874afa73be7027485fd3e99e6e729c38ba9aeee");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
