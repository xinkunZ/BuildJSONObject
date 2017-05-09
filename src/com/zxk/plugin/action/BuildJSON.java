package com.zxk.plugin.action;

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
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.zxk.plugin.util.MyConsts;
import com.zxk.plugin.util.ToJsonGenerator;

/**
 * @author zhangxinkun
 */
public class BuildJSON extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    // TODO: insert action logic here
    PsiFile currentFile = e.getData(LangDataKeys.PSI_FILE);
    if (currentFile == null) {
      return;
    }
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
      if (psiClass.findMethodsByName(MyConsts.TO_JSON, true).length > 0
          && psiClass.findMethodsByName(MyConsts.TO_JSON,
          false).length <= 0) {
        // 父类有toJson方法
        appendSuper = true;
      }

      PsiMethod toJson = elementFactory
          .createMethodFromText(ToJsonGenerator.buildToJson(psiClass.getFields(), appendSuper),
              psiClass);
      PsiMethod toString = elementFactory
          .createMethodFromText(ToJsonGenerator.buildToString(), psiClass);

      if (psiClass.findMethodsByName(toJson.getName(), false).length <= 0) {
        psiClass.add(toJson);
      }
      if (psiClass.findMethodsByName(MyConsts.TO_STRING, false).length <= 0) {
        psiClass.add(toString);
      }
      CodeStyleManager.getInstance(project).reformat(psiClass);
    });

  }

}
