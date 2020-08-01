package ede.decorate.me.decoratablePsiExpressions.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.PsiTreeUtil;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;

import java.util.Optional;

public final class DecoratableClassReference implements DecoratablePsiExpression {
    private final PsiReferenceExpression psiExpression;
    private final PsiClass psiClass;
    private final PsiType[] psiTypes;

    public DecoratableClassReference(PsiElement position) {
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

    private PsiReferenceExpression psiExpression(PsiElement position) {
        return PsiTreeUtil.getChildOfType(
                PsiTreeUtil.getParentOfType(
                        position,
                        PsiReferenceExpression.class
                ),
                PsiReferenceExpression.class
        );
    }

    /**
     * PsiClass could sometimes not be resolved
     * JetBrains docs says:
     * resolve may return null during indexing or other reference updating actions.
     */
    private PsiClass psiClass() {
        return Optional.of(psiExpression)
                       .map(PsiReferenceExpression::resolve)
                       .map(PsiVariable.class::cast)
                       .map(PsiVariable::getType)
                       .map(PsiClassReferenceType.class::cast)
                       .map(PsiClassReferenceType::resolve)
                       .orElse(null);
    }

    private PsiType[] psiTypes() {
        return new PsiType[0];
    }
}
