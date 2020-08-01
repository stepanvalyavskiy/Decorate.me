package ede.decorate.me.decoratablePsiExpressions.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.PsiTreeUtil;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;

import java.util.Optional;

public final class MethodCallExpression implements DecoratablePsiExpression {
    private final PsiMethodCallExpression psiExpression;
    private final PsiClass psiClass;
    private final PsiType[] psiTypes;

    public MethodCallExpression(PsiElement position) {
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

    private PsiMethodCallExpression psiExpression(PsiElement position) {
        return PsiTreeUtil.getChildOfType(
                PsiTreeUtil.getParentOfType(
                        position,
                        PsiReferenceExpression.class
                ),
                PsiMethodCallExpression.class
        );
    }

    /**
     * PsiClass could sometimes not be resolved
     * JetBrains docs says:
     * resolve may return null during indexing or other reference updating actions.
     */
    private PsiClass psiClass() {
        return Optional.of(psiExpression)
                       .map(PsiMethodCallExpression::getMethodExpression)
                       .map(PsiReferenceExpression::resolve)
                       .map(PsiMethod.class::cast)
                       .map(PsiMethod::getReturnType)
                       .map(PsiClassReferenceType.class::cast)
                       .map(PsiClassReferenceType::resolve)
                .orElse(null);
    }

    private PsiType[] psiTypes() {
        return Optional.of(psiExpression)
                       .map(PsiMethodCallExpression::getMethodExpression)
                       .map(PsiReferenceExpression::resolve)
                       .map(PsiMethod.class::cast)
                       .map(PsiMethod::getReturnType)
                       .map(PsiClassReferenceType.class::cast)
                       .map(PsiClassReferenceType::getParameters)
                       .orElse(new PsiType[0]);
//                       .stream()
//                       .flatMap(Arrays::stream)
//                       .map(PsiClassReferenceType.class::cast)
//                       .map(PsiClassReferenceType::resolve)
//                       .toArray(PsiClass[]::new)

    }
}
