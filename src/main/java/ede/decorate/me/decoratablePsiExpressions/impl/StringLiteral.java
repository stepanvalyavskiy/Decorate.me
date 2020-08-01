package ede.decorate.me.decoratablePsiExpressions.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.PsiTreeUtil;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;

import java.util.Optional;

public final class StringLiteral implements DecoratablePsiExpression {
    private final PsiLiteralExpression psiExpression;
    private final PsiClass psiClass;
    private final PsiType[] psiTypes;

    public StringLiteral(PsiElement position) {
        psiExpression = psiExpression(position);
        psiClass = psiClass();
        psiTypes = psiTypes();
    }

    @Override
    public PsiElement content() {
        return psiExpression;
    }

    @Override
    public PsiClass myClass() {
        return psiClass;
    }

    @Override
    public PsiType[] myTypes() {
        return psiTypes;
    }

    private PsiLiteralExpression psiExpression(PsiElement position) {
        return PsiTreeUtil.getChildOfType(
                PsiTreeUtil.getParentOfType(
                        position,
                        PsiReferenceExpression.class
                ),
                PsiLiteralExpression.class
        );
    }

    /**
     * PsiClass could sometimes not be resolved
     * JetBrains docs says:
     * resolve may return null during indexing or other reference updating actions.
     */
    private PsiClass psiClass() {
        return Optional.of(psiExpression)
                .map(PsiLiteralExpression::getType)
                .map(PsiClassReferenceType.class::cast)
                .map(PsiClassReferenceType::resolve)
                .orElse(null);
    }

    private PsiType[] psiTypes() {
        return new PsiType[0];
    }
}
