package com.keminapera.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class DbPropertiesManagerTest {
  @Test
  public void testReadProperties() throws IOException {
    DbPropertiesManager.DbProperties dbProperties = DbPropertiesManager.getDbProperties();
    String url = dbProperties.getUrl();
    String username = dbProperties.getUsername();
    String password = dbProperties.getPassword();
    Assertions.assertNotNull(url);
    Assertions.assertNotNull(username);
    Assertions.assertNotNull(password);
  }
}
