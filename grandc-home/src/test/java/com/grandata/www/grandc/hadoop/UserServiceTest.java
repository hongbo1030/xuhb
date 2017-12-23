package com.grandata.www.grandc.hadoop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.grandata.www.grandc.home.user.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext-home.xml"})
public class UserServiceTest {

  @Autowired
  private IUserService userservice;

  @Test
  public void testCreate() {
    try {
      //userservice.create("tenant2", "2");
    } catch (Exception e) {
    }
  }

}
