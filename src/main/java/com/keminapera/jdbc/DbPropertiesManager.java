package com.keminapera.jdbc;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbPropertiesManager{
  private static DbProperties dbProperties;

  public static DbProperties getDbProperties() throws IOException {
    if (dbProperties == null) {
      dbProperties = new DbProperties();
    }
    return dbProperties;
  }

  @Getter
  public static class DbProperties {
    private final String url;
    private final String username;
    private final String password;

    public DbProperties() throws IOException {
      InputStream is = ClassLoader.getSystemResourceAsStream("datasource.properties");
      Properties properties = new Properties();
      properties.load(is);
      url = properties.getProperty("url");
      username = properties.getProperty("username");
      password = properties.getProperty("password");
    }
  }

}

