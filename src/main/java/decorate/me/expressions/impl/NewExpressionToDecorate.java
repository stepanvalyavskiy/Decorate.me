package decorate.me.expressions.impl;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiReferenceExpression;
import decorate.me.expressions.ExpressionToDecorate;
import decorate.me.psiExpressions.PsiExpressionToDecorate;
import decorate.me.psiExpressions.impl.PsiNewExpressionToDecorate;

import static com.intellij.patterns.PlatformPatterns.psiElement;

@SuppressWarnings("unused")
public class NewExpressionToDecorate implements ExpressionToDecorate {

    @Override
    public PsiElementPattern.Capture<PsiIdentifier> pattern() {
        return psiElement(PsiIdentifier.class)
                .withParent(psiElement(PsiReferenceExpression.class)
                        .withChild(psiElement(PsiNewExpression.class)));
    }

    @Override
    public PsiExpressionToDecorate psiExpression(CompletionParameters completionParameters) {
        return new PsiNewExpressionToDecorate(completionParameters);
    }
}
