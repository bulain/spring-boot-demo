package com.bulain.dubbo.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bulain.dubbo.DubboConsumerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DubboConsumerApplication.class)
public class DemoServiceDemo {
	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:12345")
	private DemoService demoService;
	
	@Test
	public void testSayHello() {
		String ret = demoService.sayHello("Bulain");
		assertEquals("Hello Bulain, response form provider.", ret);
	}
	
}
