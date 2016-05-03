package org.smigo;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Test
public class SandboxTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void testAdd1() throws Exception {
//        final File file = new File("/tmp/asdf.txt");
//        final boolean newFile = file.createNewFile();
//        FileUtils.writeStringToFile(file, "as1df", Charset.defaultCharset());
        final String https = "asdf http://qwer".replaceAll(".+\\s", "");
        Assert.assertEquals(https, "http://qwer");

    }
    public void testAdd() throws Exception {
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        Assert.assertTrue(true);

    }

    @Override
    public String toString() {
        return "SandboxTest{}";
    }

    public static void main(String[] args) {
        List<SandboxTest> a = new ArrayList<>();
        a.add(new SandboxTest());
        a.add(new SandboxTest());
        System.out.println(a.toString());

    }
}
