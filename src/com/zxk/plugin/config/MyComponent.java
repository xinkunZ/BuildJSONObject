package com.zxk.plugin.config;

import javax.swing.JComponent;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;

/**
 * @author zhangxinkun
 */
@State(name = "buildJsonSettings", storages = { @Storage("buildJsonSettings.xml") })
public class MyComponent implements ProjectComponent, Configurable {

  public MyConfigBean config;

  public MyComponent(Project project) {
    this.config = ServiceManager.getService(project, MyConfigBean.class);
  }

  public MyConfigBean getConfig() {
    return config;
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

  private ConfigPanel form;

  @Nullable
  @Override
  public JComponent createComponent() {
    if (this.form == null) {
      this.form = new ConfigPanel();
    }
    this.form.setData(config == null ? new MyConfigBean() : config);
    return this.form.getRootComponent();
  }

  @Override
  public boolean isModified() {
    if (config == null) {
      return false;
    } else {
      return !config.equals(this.form.getData());
    }
  }

  @Override
  public void apply() throws ConfigurationException {
    config = this.form.getData();
  }

}
