package org.smigo.logging;

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

public class LogbackLearningTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testHelloWorld() {
        logger.trace("Hello trace.");

        logger.debug("Hello debug.");
        logger.info("Hello nfo.");
        logger.warn("Hello warn.");
        logger.error("Hello error.");
        System.out.println("sout");
        System.err.println("serr");
        Assert.assertTrue(true);
    }
}
