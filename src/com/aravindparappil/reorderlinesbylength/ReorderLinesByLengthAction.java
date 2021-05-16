package com.aravindparappil.reorderlinesbylength;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import java.util.Arrays;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

public class ReorderLinesByLengthAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
    final Document document = editor.getDocument();
    final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
    Caret primaryCaret = editor.getCaretModel()
        .getPrimaryCaret();

    if (primaryCaret.hasSelection()) {
      String selectedText = primaryCaret.getSelectedText();
      final String[] linesOfCode = selectedText.split(System.lineSeparator());

      // Sorting the array elements in ascending order of string length
      Arrays.sort(linesOfCode, Comparator.comparingInt(String::length));

      int start = primaryCaret.getSelectionStart();
      int end = primaryCaret.getSelectionEnd();

      WriteCommandAction.runWriteCommandAction(project, () -> {
        document.replaceString(start, end, String.join(System.lineSeparator(), linesOfCode));

        PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
        documentManager.commitDocument(document);

        // Indents the selected text after it has been reordered. This is equivalent to Auto-indent lines action
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);
        PsiElement psiElement = e.getData(LangDataKeys.PSI_FILE);
        PsiFile psiFile = psiElement.getContainingFile();
        TextRange textRange = new TextRange(start, end);
        codeStyleManager.adjustLineIndent(psiFile, textRange);
      });

      primaryCaret.removeSelection();
    }

  }

  /**
   * Only show the "Reorder Lines By Length" menu option when there's some text selected
   * @param e
   */
  @Override
  public void update(AnActionEvent e) {
    final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
    CaretModel caretModel = editor.getCaretModel();
    e.getPresentation().setEnabledAndVisible(caretModel.getCurrentCaret().hasSelection());
  }
}
