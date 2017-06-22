package com.zxk.plugin.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiUtil;
import com.zxk.plugin.config.MyComponent;
import com.zxk.plugin.config.MyConfigBean;

/**
 * @author zhangxinkun
 */
public class ToJsonGenerator {

  private MyConfigBean config;
  private static ToJsonGenerator instance;

  public static ToJsonGenerator getInstance(Project project) {
    if (instance == null) {
      instance = new ToJsonGenerator();
      MyComponent component = project.getComponent(MyComponent.class);
      instance.config = component.getConfig();
    }
    return instance;
  }

  public String buildToJson(PsiField[] fields, boolean appendSuper) {
    StringBuilder sb = new StringBuilder("// create by build json plugin \n");
    if (appendSuper) {
      sb.append("@Override\n");
    }
    sb.append("public JSONObject toJson(){ \n");
    if (appendSuper) {
      sb.append("JSONObject jo = super.toJson();\n");
    } else {
      sb.append("JSONObject jo = new JSONObject();\n");
    }

    for (PsiField field : fields) {
      if (field.getModifierList() != null && PsiUtil.getAccessLevel(field.getModifierList()) != MyConsts.PUBLIC_LEVEL) {
        // 只认private级别的属性
        continue;
      }
      if (ignoreField(field.getName())) {
        continue;
      }

      String fieldType = field.getType().getCanonicalText().replaceAll("<.*>", "");

      if (fieldType.toLowerCase().contains("list")) {
        // 没别的法子
        Matcher matcher = Pattern.compile("(.*<)(.*)(>.*)").matcher(field.getType().getPresentableText());
        String generic = "Object";
        if (matcher.find()) {
          generic = matcher.group(2);
        }
        sb.append(String.format("for(%s %s : %s) { \n", generic, field.getName().substring(0, 1), field.getName()));
        sb.append(String.format("jo.accumulate(\"%s\",%s);\n", field.getName(), field.getName().substring(0, 1)
            + ".toJson()"));
        sb.append("}\n");
      } else if (field.getType().equalsToText(BigDecimal.class.getName())) {
        String value = "";
        if (field.getName().toLowerCase().contains("qty")) {
          value = String.format("Converter.toQtyString(%s)", field.getName());
        } else {
          value = String.format("Converter.toMoneyString(%s)", field.getName());
        }
        sb.append(String.format("jo.put(\"%s\",%s);\n", field.getName(), value));
      } else if(field.getType().equalsToText(Date.class.getName())) {
        String value = String.format("Converter.toString(%s)", field.getName());
        sb.append(String.format("jo.put(\"%s\",%s);\n", field.getName(), value));
      } else {
        sb.append(String.format("jo.put(\"%s\",%s);\n", field.getName(), field.getName()));
      }
    }
    sb.append("return jo;}");
    return sb.toString();
  }

  public String buildToString() {
    return "@Override public String toString(){ return toJson().toString(); }";
  }

  private boolean ignoreField(String fieldName) {
    try {
      String ignoreFieldNames = config.getIgnoreFieldName();
      List<String> names = Arrays.asList(ignoreFieldNames.split(";"));
      return names.contains(fieldName);
    } catch (RuntimeException e) {
      return false;
    }
  }
}
