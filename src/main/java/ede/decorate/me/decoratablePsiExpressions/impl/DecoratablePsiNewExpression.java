package ede.decorate.me.decoratablePsiExpressions.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;

import java.util.Optional;

public final class DecoratablePsiNewExpression implements DecoratablePsiExpression {
    private final PsiNewExpression psiExpression;
    private final PsiClass psiClass;
    private final PsiType[] psiTypes;

    public DecoratablePsiNewExpression(PsiElement position) {
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

    private PsiNewExpression psiExpression(PsiElement position) {
        return PsiTreeUtil.getChildOfType(
                PsiTreeUtil.getParentOfType(
                        position,
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

    private PsiType[] psiTypes() {
        return Optional.of(psiExpression)
                       .map(PsiNewExpression::getClassReference)
                       .map(PsiJavaCodeReferenceElement::getTypeParameters)
                       .orElse(new PsiType[0]);
    }
}
