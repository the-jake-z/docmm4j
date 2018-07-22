package com.jzarob.docmm4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Docmm4jApplicationTests {

	@Test
	public void contextLoads() {
        Assert.assertTrue("context was loaded" ,true);
	}

}
