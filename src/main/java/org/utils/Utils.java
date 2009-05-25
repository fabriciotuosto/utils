package org.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.text.StrTokenizer;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class Utils {
	
	static{
		MethodUtils.setCacheMethods(true);
	}

	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String[] getTokens(String string) 
	{
		return getTokens(string,",");
	}
	/**
	 * 
	 * @param string
	 * @param token
	 * @return
	 */
	public static String[] getTokens(String string,String token) {
		return new StrTokenizer(token,string).getTokenArray();
	}
	
	/**
	 * 
	 * @param string
	 * @param token
	 * @return
	 */
	public static List<String> getTokenList(String string, String token)
	{
		return Lists.newArrayList(getTokens(string,token));
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static List<String> getTokenList(String string)
	{
		return Lists.newArrayList(getTokens(string));
	}	
	
	/**
	 * 
	 * @param <T>
	 * @param toClone
	 * @return
	 */
	public static <T> List<T> cloneList(List<T> toClone)
	{
		return Lists.transform(toClone, new Function<T, T>(){
			@Override
			public T apply(T from) {
				return Utils.cloneBean(from);
			}});
	}
	
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneBean(T bean)
	{
		try {
			return (T) BeanUtils.cloneBean(bean);
		} catch (Exception e) {
			throw new RuntimeException("No se pudo clonar el bean "+bean,e);
		}
	}
	
	/**
	 * Invokes via reflection close method with no parameters
	 * @param <T>
	 * @param closableItmes
	 * @throws Exception if the method close without parameters doesn't exist in the Class of the parameter
	 * or invoking the close method throws an exceptions
	 */
	public static <T> void close(T... closableItmes) throws Exception
	{
		Exception exception = null;
		for (T t : closableItmes)
		{
			if (t != null){
				try{
					invokeMethod("close", t);
				}catch (Exception e)
				{
					exception = e;
				}
			}
		}
		if ( exception != null) throw exception;
	}
	
	/**
	 * 
	 * @param method
	 * @param target
	 * @throws Exception
	 */
	private static void invokeMethod(String method, Object target) throws Exception
	{
		MethodUtils.invokeExactMethod(target, method, null);
	}
	
	/**
	 * Invokes via reflection close method with no parameters
	 * @param <T>
	 * @param closableItmes
	 */
	public static <T> void closeQuietly(T... closableItmes)
	{
		try{
			close(closableItmes);
		}catch (Exception e){} // shhhhh !!
	}
	
	/**
	 * 
	 * @param <T>
	 * @param coleccion
	 * @param defaul
	 * @return
	 */
	public static <T> T getCollectionFistElement(Collection<T> coleccion,T defaul)
	{
		Iterator<T> iterator = coleccion.iterator();
		return iterator.hasNext() ? iterator.next() : defaul;
	}
}
