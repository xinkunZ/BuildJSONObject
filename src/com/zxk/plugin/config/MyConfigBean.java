package com.zxk.plugin.config;

/**
 * @author zhangxinkun
 */
public class MyConfigBean {

  private String ignoreFieldName = "tableName;logger;messagePrefix;";

  public String getIgnoreFieldName() {
    return ignoreFieldName;
  }

  public void setIgnoreFieldName(String ignoreFieldName) {
    this.ignoreFieldName = ignoreFieldName;
  }
}
