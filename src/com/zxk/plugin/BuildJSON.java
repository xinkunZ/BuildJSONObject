package com.zxk.plugin;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangxinkun
 */
public class BuildJSON extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    // TODO: insert action logic here
    PsiFile currentFile = e.getData(LangDataKeys.PSI_FILE);
    if (!JavaLanguage.INSTANCE.is(currentFile.getLanguage())) {
      return;
    }
    WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
      Project project = e.getData(PlatformDataKeys.PROJECT);
      if (project == null) {
        return;
      }
      PsiElement element = currentFile
          .findElementAt(e.getData(PlatformDataKeys.EDITOR).getCaretModel().getOffset());
      PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
      PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
      boolean appendSuper = false;
      if (psiClass.findMethodsByName("toJson", true).length > 0
          && psiClass.findMethodsByName("toJson",
          false).length <= 0) {
        appendSuper = true;
      }

      PsiMethod toJson = elementFactory
          .createMethodFromText(buildToJson(project, psiClass.getFields(), appendSuper),
              psiClass);
      PsiMethod toString = elementFactory.createMethodFromText(buildToString(), psiClass);

      if (psiClass.findMethodsByName(toJson.getName(), true).length <= 0) {
        psiClass.add(toJson);
      }
      if (psiClass.findMethodsByName("toString", false).length <= 0) {
        psiClass.add(toString);
      }
      CodeStyleManager.getInstance(project).reformat(psiClass);
    });

  }

  public static void main(String[] args) {
    Matcher matcher = Pattern.compile("<.*>").matcher("java.util.List<java.lang.String>");

  }

  private String buildToJson(Project project, PsiField[] fields, boolean appendSuper) {
    StringBuilder sb = new StringBuilder("// create by build json plugin \n");
    sb.append("public String toJson(){ \n");
    if (appendSuper) {
      sb.append("JSONObject jo = super.toJson();\n");
    } else {
      sb.append("JSONObject jo = new JSONObject();\n");
    }

    for (PsiField field : fields) {

      String fieldType = field.getType().getCanonicalText().replaceAll("<.*>", "");

      if (fieldType.toLowerCase().contains("list")) {
        //没别的法子
        Matcher matcher = Pattern.compile("(.*<)(.*)(>.*)")
            .matcher(field.getType().getPresentableText());
        String generic = "Object";
        if (matcher.find()) {
          generic = matcher.group(2);
        }
        sb.append(String.format("for(%s l : %s) { \n", generic, field.getName()));
        sb.append(String
            .format("jo.accumulate(\"%s\",%s);\n", field.getName(), field.getName() + ".toJson()"));
        sb.append("}\n");
      } else {
        sb.append(String.format("jo.put(\"%s\",%s);\n", field.getName(), field.getName()));
      }
    }
    sb.append("return jo.toString();}");
    return sb.toString();
  }

  private String buildToString() {
    return "public String toString(){ return toJson().toString(); }";
  }

}
