package com.zxk.plugin.action;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.zxk.plugin.util.MyConsts;
import com.zxk.plugin.util.ToJsonGenerator;
import com.zxk.plugin.util.MyConsts.NeedImport;

/**
 * @author zhangxinkun
 */
public class BuildJSON extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    PsiFile currentFile = e.getData(LangDataKeys.PSI_FILE);
    if (currentFile == null) {
      return;
    }
    if (!JavaLanguage.INSTANCE.is(currentFile.getLanguage())) {
      return;
    }
    WriteCommandAction.runWriteCommandAction(
        e.getProject(),
        () -> {
          Project project = e.getData(PlatformDataKeys.PROJECT);
          if (project == null) {
            return;
          }
          PsiJavaFile psiJavaFile = (PsiJavaFile) currentFile;
          PsiElement element = currentFile
              .findElementAt(e.getData(PlatformDataKeys.EDITOR).getCaretModel().getOffset());
          PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
          PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
          boolean appendSuper = false;
          if (psiClass.findMethodsByName(MyConsts.TO_JSON, true).length > 0
              && psiClass.findMethodsByName(MyConsts.TO_JSON, false).length <= 0) {
            // 父类有toJson方法
            appendSuper = true;
          }

          PsiMethod toJson = elementFactory.createMethodFromText(
              ToJsonGenerator.getInstance(project).buildToJson(psiClass.getFields(), appendSuper), psiClass);
          PsiMethod toString = elementFactory.createMethodFromText(
              ToJsonGenerator.getInstance(project).buildToString(), psiClass);

          if (psiClass.findMethodsByName(toJson.getName(), false).length <= 0) {
            psiClass.add(toJson);
          }
          if (psiClass.findMethodsByName(MyConsts.TO_STRING, false).length <= 0) {
            psiClass.add(toString);
          }
          importClass(project, psiJavaFile, NeedImport.converter);
          importClass(project, psiJavaFile, NeedImport.jsonObject);
          JavaCodeStyleManager.getInstance(project).optimizeImports(psiJavaFile);
        });
  }

  private static void importClass(Project project, PsiJavaFile javaFile, MyConsts.NeedImport importClass) {
    PsiClass[] converters = PsiShortNamesCache.getInstance(project).getClassesByName(importClass.name,
        GlobalSearchScope.allScope(project));
    PsiClass jposConverter = null;
    for (PsiClass aClass : converters) {
      if (StringUtil.equals(aClass.getQualifiedName(), importClass.qualifiedName)) {
        jposConverter = aClass;
        break;
      }
    }
    if (jposConverter != null) {
      PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
      PsiImportStatement importStatement = factory.createImportStatement(jposConverter);
      javaFile.getImportList().add(importStatement);
    }
  }

}
