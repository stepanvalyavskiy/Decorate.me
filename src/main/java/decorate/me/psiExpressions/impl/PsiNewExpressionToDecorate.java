package decorate.me.psiExpressions.impl;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.util.PsiTreeUtil;
import decorate.me.psiExpressions.PsiExpressionToDecorate;

import java.util.Optional;

public class PsiNewExpressionToDecorate implements PsiExpressionToDecorate {
    private final PsiNewExpression psiExpression;
    private final PsiClass psiClass;

    public PsiNewExpressionToDecorate(CompletionParameters completionParameters) {
        psiExpression = psiExpression(completionParameters);
        psiClass = psiClass();
    }

    @Override
    public PsiNewExpression content() {
        return psiExpression;
    }

    @Override
    public PsiClass myClass() {
        return psiClass;
    }

    private PsiNewExpression psiExpression(CompletionParameters completionParameters) {
        return PsiTreeUtil.getChildOfType(
                PsiTreeUtil.getParentOfType(
                        completionParameters.getPosition(),
                        PsiReferenceExpression.class
                ),
                PsiNewExpression.class
        );
    }

    /**
     * PsiClass could sometimes not be resolved
     * JetBrains docs says:
     * resolve may return null during indexing or other reference updating actions.
     */
    private PsiClass psiClass() {
        return Optional.of(psiExpression)
                       .map(PsiNewExpression::getClassReference)
                       .map(PsiJavaCodeReferenceElement::resolve)
                       .map(PsiClass.class::cast)
                       .orElse(null);
    }
}
