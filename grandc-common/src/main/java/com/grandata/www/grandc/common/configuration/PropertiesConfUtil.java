package com.grandata.www.grandc.common.configuration;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.reloading.ReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesConfUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesConfUtil.class);

    public static final String CONFIGURATION_ENCODING = "UTF-8";

    public static String filePath = "";

    private static PropertiesConfUtil pcUtil;

    private static PropertiesConfiguration propertiesConfiguration;

    private PropertiesConfUtil(){}

    //不抛异常
    public synchronized static PropertiesConfUtil getInstance() {
        try{
            if(pcUtil == null){
                propertiesConfiguration = new PropertiesConfiguration();
                propertiesConfiguration.setEncoding(CONFIGURATION_ENCODING);
                propertiesConfiguration.setFileName(filePath);
                propertiesConfiguration.setDelimiterParsingDisabled(true);

                //当配置文件修改时，自动加载，不需重启系统
                ReloadingStrategy strategy = new FileChangedReloadingStrategy();
                strategy.setConfiguration(propertiesConfiguration);
                propertiesConfiguration.setReloadingStrategy(strategy);

                propertiesConfiguration.load();

                pcUtil = new PropertiesConfUtil();

                logger.info("初始化配置文件成功");
            }
        }catch(Exception e){
            logger.error("初始化配置文件失败", e);
            //throw e;
        }

        return pcUtil;
    }

    /**
     * 根据key获取对应的值
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String result = "";
        if(key != null && !key.equals("")){
            Object obj = propertiesConfiguration.getProperty(key);
            if(obj != null){
                result = String.valueOf(obj);
            }
        }
        return result.trim();
    }

    public String getString(String key, String defaultValue){
    	String value = getProperty(key);
        if( value == null || value.equals("")) {
            return defaultValue;
        }
        else{
        	return value;
        }
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean containsKey(String key){
        return propertiesConfiguration.containsKey(key);
    }

    public int getIntValue(String key, int defaultValue) {
        String value = getProperty(key);
        if( value == null || value.equals("")) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public long getLongValue(String key, long defaultValue) {
        String value = getProperty(key);
        if( value == null || value.equals("")) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null || value.isEmpty())
            return defaultValue;
        return Boolean.valueOf(value);
    }
}
