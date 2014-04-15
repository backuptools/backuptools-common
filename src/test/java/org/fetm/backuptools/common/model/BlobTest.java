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

package org.fetm.backuptools.common.model;

import junit.framework.TestCase;

/**
 * Created by florian on 19.03.14.
 */
public class BlobTest extends TestCase {
    public void testGetName() throws Exception {
        String name = "test";
        Blob blob = new Blob(name);
        assertEquals(name,blob.getName());
    }
}
