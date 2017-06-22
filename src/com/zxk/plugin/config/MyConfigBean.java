package com.zxk.plugin.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;

/**
 * @author zhangxinkun
 */
public class MyConfigBean implements PersistentStateComponent<MyConfigBean>, Comparable<MyConfigBean> {

  private String ignoreFieldName = "tableName;logger;messagePrefix;serialVersionUID";

  public String getIgnoreFieldName() {
    return ignoreFieldName;
  }

  public void setIgnoreFieldName(String ignoreFieldName) {
    this.ignoreFieldName = ignoreFieldName;
  }

  @Nullable
  @Override
  public MyConfigBean getState() {
    return this;
  }

  @Override
  public void loadState(MyConfigBean myConfigBean) {
    XmlSerializerUtil.copyBean(myConfigBean, this);
  }

  @Override
  public int compareTo(@NotNull MyConfigBean o) {
    if (!StringUtil.equals(this.getIgnoreFieldName(), o.getIgnoreFieldName())) {
      return -1;
    }
    return 0;
  }
}
