package com.grandata.www.grandc.common.configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String, Date> {

	private static final Log LOG = LogFactory.getLog(StringToDateConverter.class);
	
	@Override
	public Date convert(String source) {
		if (source == null  || "".equals(source)) {
			return null;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");    
	    dateFormat.setLenient(false);    
	    try {    
	        return dateFormat.parse(source);    
	    } catch (ParseException e) {    
	    	LOG.error(source, e);  
	    } 
	    
	    return null; 
	}

}
