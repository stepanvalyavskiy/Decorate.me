package ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression.impl;

import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiReferenceExpression;
import ede.decorate.me.decoratablePsiExpressions.DecoratablePsiExpression;
import ede.decorate.me.decoratablePsiExpressions.impl.DecoratableClassReference;
import ede.decorate.me.decoratablePsiExpressions.patternToDecoratablePsiExpression.PatternToDecoratablePsiExpression;

import static com.intellij.patterns.PlatformPatterns.psiElement;

@SuppressWarnings("unused")
public final class ClassReference implements PatternToDecoratablePsiExpression {
    @Override
    public ElementPattern<? extends PsiElement> pattern() {
        return psiElement(PsiIdentifier.class)
                .withParent(psiElement(PsiReferenceExpression.class)
                        .withChild(psiElement(PsiReferenceExpression.class)));
    }

    @Override
    public DecoratablePsiExpression psiDecoratableExpression(PsiElement position) {
        return new DecoratableClassReference(position);
    }
}
