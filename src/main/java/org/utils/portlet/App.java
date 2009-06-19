package org.utils.portlet;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.Annotation;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

/**
 * 
 * 
 */
public class App {
	
	
	
	@SuppressWarnings({ "unused", "unchecked" })
	private void ParserXmlPorlet(){
		
		URL xmlFile = ClassLoader.getSystemResource("portlet.xml");
		List<String> classNames = new LinkedList<String>();
        Document document = null; 
		try {
			document = new SAXReader().read(xmlFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}		
    	XPath xpathSelector = DocumentHelper.createXPath("//init-param[name='beanClass']/value");
    	List results = xpathSelector.selectNodes(document);
    	for ( Iterator iter = results.iterator(); iter.hasNext(); )
    	{
    		Element element = (Element) iter.next();
    		System.out.println(element.getText().trim());
    	    classNames.add(element.getText().trim());
    	}
	}
	
	
	@SuppressWarnings({ "unused", "unchecked" })
	private void findAnnotations(List<Class> classNames){
		
		for(Object beanCrud : classNames){
			
			Class clazz = beanCrud.getClass();
			Method [] methods = clazz.getMethods();
			
			Method method = null;
			Annotation [] annotations = null;
			for(int i = 0; i < methods.length ; i++){
				method = methods[i];
				method.getAnnotations();
			}
			
			
			
			
		}
	}
    	
}
