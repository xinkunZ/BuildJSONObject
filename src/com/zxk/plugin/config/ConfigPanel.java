package com.zxk.plugin.config;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author zhangxinkun
 */
public class ConfigPanel {
  private JTextField ignoreField;
  private JPanel cp;

  public void setData(MyConfigBean data) {
    ignoreField.setText(data.getIgnoreFieldName());
  }

  public JComponent getRootComponent() {
    return cp;
  }

  public MyConfigBean getData() {
    MyConfigBean data = new MyConfigBean();

    data.setIgnoreFieldName(ignoreField.getText());
    return data;
  }
}
