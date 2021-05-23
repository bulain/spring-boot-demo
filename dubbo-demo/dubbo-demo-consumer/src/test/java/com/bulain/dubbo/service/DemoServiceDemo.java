package com.bulain.dubbo.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bulain.dubbo.DubboConsumerApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
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
