package com.zxk.plugin.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author zhangxinkun
 */
@State(name = "buildJsonSettings", storages = {@Storage("buildJsonSettings.xml")})
public class MyComponent implements ProjectComponent, Configurable,
    PersistentStateComponent<MyConfigBean> {

  private Project project;
  public static MyConfigBean config = new MyConfigBean();

  public MyComponent(Project project) {
    this.project = project;
  }

  @Override
  public void initComponent() {
    // TODO: insert component initialization logic here
  }

  @Override
  public void disposeComponent() {
    // TODO: insert component disposal logic here
  }

  @Override
  @NotNull
  public String getComponentName() {
    return "com.zxk.plugin.config.MyComponent";
  }

  @Override
  public void projectOpened() {
    // called when project is opened
  }

  @Override
  public void projectClosed() {
    // called when project is being closed
  }

  @Nls
  @Override
  public String getDisplayName() {
    return "build json";
  }

  private JTextField t;

  @Nullable
  @Override
  public JComponent createComponent() {
    JPanel panel = new JPanel(new FlowLayout());
    t = new JTextField();
    t.setColumns(18);
    panel.add(t);

    return panel;
  }

  @Override
  public boolean isModified() {
    return !config.getIgnoreFieldName().equals(t.getText());
  }

  @Override
  public void apply() throws ConfigurationException {
    config.setIgnoreFieldName(t.getText());
  }


  @Nullable
  @Override
  public MyConfigBean getState() {
    return config;
  }

  @Override
  public void reset() {
    t.setText(config.getIgnoreFieldName());
  }

  @Override
  public void loadState(MyConfigBean myConfigBean) {
    XmlSerializerUtil.copyBean(myConfigBean, config);
  }
}
