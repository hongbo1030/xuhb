package com.grandata.www.grandc.home.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import com.grandata.www.grandc.home.user.controller.UserController;

public class HomeInitListener implements ServletContextListener {

  private static Logger logger = Logger.getLogger(UserController.class);

  @Override
  public void contextInitialized(ServletContextEvent arg0) {

    logger.info("HomeInitListener init start");

    try {
      //IUserService userservice = (IUserService) SpringContextUtil.getBean("userservice");
    } catch (Exception e) {
      logger.error("Spring bean error", e);
    }

    logger.info("HomeInitListener init end");
  }

  @Override
  public void contextDestroyed(ServletContextEvent arg0) {
    logger.info("HomeInitListener destroy start");
    // nothing
    logger.info("HomeInitListener destroy end");
  }

}
