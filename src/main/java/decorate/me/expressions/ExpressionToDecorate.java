package decorate.me.expressions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import decorate.me.psiExpressions.PsiExpressionToDecorate;

public interface ExpressionToDecorate {
    ElementPattern<? extends PsiElement> pattern();
    PsiExpressionToDecorate psiExpression(CompletionParameters completionParameters);
}
