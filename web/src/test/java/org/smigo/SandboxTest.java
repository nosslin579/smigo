package org.smigo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class SandboxTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private BCryptPasswordEncoder a = new BCryptPasswordEncoder();

    public void testAdd() throws Exception {
        a.matches("testreg17", "$2a$10$r1TvEVQWyOYJJ3yvc8qLbOyA3LdsrM63bPFjoOB/ITEXGllcV4iI.");
        Assert.assertTrue(true);

    }
}
