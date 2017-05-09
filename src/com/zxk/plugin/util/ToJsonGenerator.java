package com.zxk.plugin.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiUtil;
import com.zxk.plugin.config.MyComponent;

/**
 * @author zhangxinkun
 */
public class ToJsonGenerator {

  public static String buildToJson(PsiField[] fields, boolean appendSuper) {
    StringBuilder sb = new StringBuilder("// create by build json plugin \n");
    if (appendSuper) {
      sb.append("@Override");
    }
    sb.append("public String toJson(){ \n");
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
      } else {
        sb.append(String.format("jo.put(\"%s\",%s);\n", field.getName(), field.getName()));
      }
    }
    sb.append("return jo.toString();}");
    return sb.toString();
  }

  public static String buildToString() {
    return "@Override public String toString(){ return toJson(); }";
  }

  private static boolean ignoreField(String fieldName) {
    try {
      String ignoreFieldNames = MyComponent.config.getIgnoreFieldName();
      List<String> names = Arrays.asList(ignoreFieldNames.split(";"));
      return names.contains(fieldName);
    } catch (RuntimeException e) {
      return false;
    }
  }
}
