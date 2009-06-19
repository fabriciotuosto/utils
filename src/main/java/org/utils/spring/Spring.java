package org.utils.spring;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.google.inject.spring.SpringIntegration;

public class Spring {

	private static Spring instance = new Spring();
	private static String[] config = {"classpath:/ctx/application.xml" };
	private static ApplicationContext context = new ClassPathXmlApplicationContext(config);
	
	private Spring()
	{
		SpringIntegration.bindAll(null, context);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Spring getInstance()
	{
		return instance;
	}
	
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <E> E getBean(String beanName)
	{
		return (E) context.getBean(beanName);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public Resource getResource(String path) {
		return context.getResource(path);
	}
	
	/**
	 * 
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	public String getResourcePath(String resource) throws Exception
	{
		return getResource(resource).getFile().getAbsolutePath();
	}
	
}
