package com.grandata.www.grandc.common.configuration.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





import com.grandata.www.grandc.common.configuration.PropertiesConfUtil;
import com.grandata.www.grandc.common.configuration.ResourceUtils;
import com.grandata.www.grandc.common.configuration.ThreadPoolService;

public class ConfigurationListener implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(ConfigurationListener.class);
    
    private final static String CONFIGURATION_LOCATION = "configurationLocation";

    public final void contextInitialized(ServletContextEvent event) {
        this.initConfiguration(event.getServletContext());
    }

    public final void contextDestroyed(ServletContextEvent sce) {
    	logger.info("ThreadPoolService 关闭");
    	ThreadPoolService.getInstance().shutdown();
    	logger.info("ThreadPoolService 关闭结束");
    }

    /**
     * initConfiguration
     * 
     * @param servletContext
     *            ServletContext
     */
    private void initConfiguration(ServletContext servletContext) {
        String location = servletContext.getInitParameter(CONFIGURATION_LOCATION);
        if (location != null && !location.equals("")) {
            try {
                if (!ResourceUtils.isUrl(location)) {
                    location = ResourceUtils.getRealPath(servletContext, location);
                }
                else{
                    location = ResourceUtils.getURL(location).getPath();
                }
                
                PropertiesConfUtil.filePath = location;
                PropertiesConfUtil.getInstance();

            } catch (Exception e) {
                logger.error("初始化配置文件出现异常", e);
            }
        } else {
            logger.error("配置文件路径没有有配，不初始化配置");
        }
    }

}
