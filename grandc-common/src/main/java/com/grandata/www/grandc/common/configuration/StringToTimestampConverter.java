package com.grandata.www.grandc.common.configuration;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToTimestampConverter implements Converter<String, Timestamp> {

	private static final Log LOG = LogFactory.getLog(StringToTimestampConverter.class);

	public static String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public Timestamp convert(String source) {
		if (source == null  || "".equals(source)) {
			return null;
		}

		SimpleDateFormat datetimeFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
		datetimeFormat.setLenient(false);
	    try {
	        return new Timestamp(datetimeFormat.parse(source).getTime());
	    } catch (ParseException e) {
	    	LOG.error(source, e);
	    }

	    return null;
	}

}
