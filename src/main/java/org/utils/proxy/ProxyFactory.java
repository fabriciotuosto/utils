package org.utils.proxy;

import org.apache.commons.proxy.Invoker;

public final class ProxyFactory {

	private ProxyFactory(){}
	
	public static <T> T newProxyInstance(T object,Invoker handler)
	{
		Class<?>[] interfaces = object.getClass().getInterfaces();
		@SuppressWarnings("unchecked")
		T proxy = (T) new org.apache.commons.proxy.ProxyFactory().createInvokerProxy(handler, interfaces);
		return proxy;
	}
}
