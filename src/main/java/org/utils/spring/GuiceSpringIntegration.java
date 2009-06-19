package org.utils.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.AbstractModule;
import com.google.inject.spring.SpringIntegration;

public class GuiceSpringIntegration extends AbstractModule{

	private static String[] config = {"classpath:/ctx/application.xml" };
	private static ApplicationContext context = new ClassPathXmlApplicationContext(config);

	public GuiceSpringIntegration() {
	}

	@Override
	protected void configure() {
		SpringIntegration.bindAll(binder(), context);
		
	}
}
