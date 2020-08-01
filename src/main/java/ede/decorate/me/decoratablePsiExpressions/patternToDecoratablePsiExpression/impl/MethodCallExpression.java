package ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression.impl;

import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;
import ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression.PatternToDecoratablePsiExpression;

import static com.intellij.patterns.PlatformPatterns.psiElement;

@SuppressWarnings("unused")
public final class MethodCallExpression implements PatternToDecoratablePsiExpression {
    @Override
    public ElementPattern<? extends PsiElement> pattern() {
        return psiElement(PsiIdentifier.class)
                .withParent(psiElement(PsiReferenceExpression.class)
                        .withChild(psiElement(PsiMethodCallExpression.class)));
    }

    @Override
    public DecoratablePsiExpression psiDecoratableExpression(PsiElement position) {
        return new ede.decorate.me.decoratablePsiExpressions.impl.MethodCallExpression(position);
    }
}
