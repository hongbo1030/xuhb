package com.grandata.www.grandc.common.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

public final class ResourceUtils {
	
	/**
	 * private ResourceUtils constructor
	 */
	private ResourceUtils(){}
	
	private final static String CLASSPATH_URL_PREFIX = "classpath:";
	
	/**
	 * isUrl
	 * @param resourceLocation resourceLocation
	 * @return boolean
	 */
	public static boolean isUrl(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			return true;
		}
		try {
			new URL(resourceLocation);
			return true;
		}
		catch (MalformedURLException ex) {
			return false;
		}
	}
	
	/**
	 * 
	 * @param resourceLocation resourceLocation
	 * @return URL
	 * @throws FileNotFoundException FileNotFoundException
	 */
	public static URL getURL(String resourceLocation) throws FileNotFoundException {
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			URL url = getDefaultClassLoader().getResource(path);
			if (url == null) {
				String description = "class path resource [" + path + "]";
				throw new FileNotFoundException(
						description + " cannot be resolved to URL because it does not exist");
			}
			return url;
		}
		boolean exceptionFlag = true;
		try {
			// try URL
			return new URL(resourceLocation);
		}
		catch (MalformedURLException ex) {
			// no URL -> treat as file path
			try {
				return new File(resourceLocation).toURI().toURL();
			}
			catch (MalformedURLException ex2) {
				exceptionFlag = true;
			}
			
		}
		if(exceptionFlag){
			throw new FileNotFoundException("Resource location [" + resourceLocation +
			"] is neither a URL not a well-formed file path");
		}
		return null;
	}
	
	/**
	 * getDefaultClassLoader
	 * @return ClassLoader
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Exception ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = ResourceUtils.class.getClassLoader();
		}
		return cl;
	}
	
	/**
	 * getRealPath
	 * @param servletContext servletContext
	 * @param path path
	 * @return String
	 * @throws FileNotFoundException FileNotFoundException
	 */
	public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
		String filePath = path;
		if (!filePath.startsWith("/")) {
			filePath = "/" + path;
		}
		String realPath = servletContext.getRealPath(filePath);
		if (realPath == null) {
			throw new FileNotFoundException(
					"ServletContext resource [" + filePath + "] cannot be resolved to absolute file path - " +
					"web application archive not expanded?");
		}
		return realPath;
	}

}
